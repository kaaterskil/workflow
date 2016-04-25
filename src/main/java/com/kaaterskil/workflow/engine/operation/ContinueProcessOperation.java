package com.kaaterskil.workflow.engine.operation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.bpm.common.SequenceFlow;
import com.kaaterskil.workflow.bpm.common.event.BoundaryEvent;
import com.kaaterskil.workflow.bpm.common.event.CompensateEventDefinition;
import com.kaaterskil.workflow.bpm.common.process.Process;
import com.kaaterskil.workflow.engine.behavior.BehaviorHelper;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.ActivityBehavior;
import com.kaaterskil.workflow.engine.delegate.TokenListener;
import com.kaaterskil.workflow.engine.delegate.event.SequenceFlowTakenWorkflowEvent;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventFactory;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventType;
import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.engine.executor.JobHandler;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.MessageEntity;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.util.ProcessDefinitionUtil;
import com.kaaterskil.workflow.util.CollectionUtil;

public class ContinueProcessOperation extends AbstractOperation {
    private static final Logger log = LoggerFactory.getLogger(ContinueProcessOperation.class);

    private final boolean forceSynchronous;
    private final boolean inCompensation;

    public ContinueProcessOperation(CommandContext commandContext, Token token,
            boolean forceSynchronous, boolean inCompensation) {
        super(commandContext, token);
        this.forceSynchronous = forceSynchronous;
        this.inCompensation = inCompensation;
    }

    public ContinueProcessOperation(CommandContext commandContext, Token token) {
        this(commandContext, token, false, false);
    }

    @Override
    public void run() {
        FlowElement currentFlowElement = token.getCurrentFlowElement();
        if (currentFlowElement == null) {
            currentFlowElement = findCurrentFlowElement(token);
            token.setCurrentFlowElement(currentFlowElement);
        }

        if (currentFlowElement instanceof FlowNode) {
            // Test if this is the initial flow node
            final FlowNode currentFlowNode = (FlowNode) currentFlowElement;
            if (currentFlowNode.getIncoming() != null
                    && currentFlowNode.getIncoming().size() == 0) {
                executeProcessStartTokenListeners();
            }
            continueThroughFlowNode(currentFlowNode);
        } else if (currentFlowElement instanceof SequenceFlow) {
            continueThroughSequenceFlow((SequenceFlow) currentFlowElement);
        } else {
            throw new WorkflowException(
                    "No current flow element found, or invalid type: " + currentFlowElement);
        }
    }

    private void executeProcessStartTokenListeners() {
        final Process process = ProcessDefinitionUtil.getProcess(token.getProcessDefinitionId());
        executeTokenListeners(process, TokenListener.EVENT_START);
    }

    private void continueThroughFlowNode(FlowNode flowNode) {
        if (!forceSynchronous) {
            final boolean isAsynchronous = flowNode.isAsynchronous();
            if (isAsynchronous) {
                scheduleJob();
                return;
            }
        }

        if (CollectionUtil.isNotEmpty(flowNode.getTokenListeners())) {
            executeTokenListeners(flowNode, TokenListener.EVENT_START);
        }

        if (!inCompensation) {
            final List<BoundaryEvent> boundaryEvents = findBoundaryEventsForFlowNode(
                    token.getProcessDefinitionId(), flowNode);
            if (CollectionUtil.isNotEmpty(boundaryEvents)) {
                executeBoundaryEvents(boundaryEvents, token);
            }
        }

        final BehaviorHelper behaviorHelper = Context.getProcessEngineService()
                .getBehaviorHelper(token);
        final ActivityBehavior behavior = behaviorHelper.getBehavior(flowNode);
        if (behavior != null) {
            log.debug("Executing activityBehavior {} on activity {} with token {}",
                    behavior.getClass().getSimpleName(), flowNode.getId(), token.getId());

            Context.getProcessEngineService().getEventDispatcher()
                    .dispatchEvent(WorkflowEventFactory.createActivityEvent(
                            WorkflowEventType.ACTIVITY_STARTED, flowNode.getId(),
                            flowNode.getName(), token, flowNode));

            try {
                behavior.execute(token);
            } catch (final Exception e) {
                log.error("Error: Activity Behavior {} on activity {} with token {}", behavior,
                        flowNode.getId(), token.getId());
                throw e;
            }
        } else {
            log.debug("No activity behavior on activity {} in token {}", flowNode.getId(),
                    token.getId());
            workflow.addOutgoingSequenceFlow(token, true);
        }
    }

    private List<BoundaryEvent> findBoundaryEventsForFlowNode(Long processDefinitionId,
            FlowNode flowNode) {
        final Process process = ProcessDefinitionUtil.getProcess(processDefinitionId);

        final List<BoundaryEvent> result = new ArrayList<>();
        final List<BoundaryEvent> boundaryEvents = process.findFlowElements(BoundaryEvent.class);
        for (final BoundaryEvent event : boundaryEvents) {
            if (event.getAttachedToRef() != null
                    && event.getAttachedToRef().equals(flowNode.getId())) {
                result.add(event);
            }
        }
        return result;
    }

    private void executeBoundaryEvents(List<BoundaryEvent> boundaryEvents, Token token) {
        for (final BoundaryEvent event : boundaryEvents) {
            if (CollectionUtil.isEmpty(event.getEventDefinitions())) {
                continue;
            }

            if (event.getEventDefinitions().get(0) instanceof CompensateEventDefinition) {
                continue;
            }

            final Token childToken = commandContext.getTokenService().createChildToken(token);
            childToken.setParent(token);
            childToken.setCurrentFlowElement(event);
            childToken.setScope(false);

            final BehaviorHelper behaviorHelper = Context.getProcessEngineService()
                    .getBehaviorHelper(token);
            final ActivityBehavior behavior = behaviorHelper.getBehavior(event);
            try {
                behavior.execute(childToken);
            } catch (final Exception e) {
                throw new WorkflowException(
                        "Could not execute boundary event behavior " + event.getBehavior());
            }
        }
    }

    private void continueThroughSequenceFlow(SequenceFlow sequenceFlow) {
        if (CollectionUtil.isNotEmpty(sequenceFlow.getTokenListeners())) {
            executeTokenListeners(sequenceFlow, TokenListener.EVENT_TRANSITION);
        }

        final FlowElement sourceFlowElement = sequenceFlow.getSourceFlowElement();
        final FlowElement targetFlowElement = sequenceFlow.getTargetFlowElement();
        final String sourceFlowBehavior = sourceFlowElement != null
                ? ((FlowNode) sourceFlowElement).getBehavior() : null;
        final String targetFlowBehavior = targetFlowElement != null
                ? ((FlowNode) targetFlowElement).getBehavior() : null;

        final SequenceFlowTakenWorkflowEvent event = WorkflowEventFactory
                .createSequenceFlowTakenEvent(token, WorkflowEventType.SEQUENCE_FLOW_TAKEN,
                        sequenceFlow.getId(), sourceFlowElement, sourceFlowBehavior,
                        targetFlowElement, targetFlowBehavior);
        Context.getProcessEngineService().getEventDispatcher().dispatchEvent(event);

        token.setCurrentFlowElement(targetFlowElement);

        log.debug("Sequence Flow {} encountered. Continuing process using token {}",
                sequenceFlow.getId(), token.getId());

        workflow.addContinueProcessOperation(token);
    }

    private void scheduleJob() {
        final MessageEntity job = commandContext.getJobService().createMessage();
        job.setTokenId(token.getId());
        job.setProcessInstanceId(token.getProcessInstanceId());
        job.setProcessDefinitionId(token.getProcessDefinitionId());
        job.setJobHandlerType(JobHandler.ASYNC_TYPE);

        commandContext.getJobService().send(job);
    }

}
