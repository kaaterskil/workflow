package com.kaaterskil.workflow.engine.util;

import java.util.Map;

import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.bpm.common.activity.EventSubProcess;
import com.kaaterskil.workflow.bpm.common.event.EventDefinition;
import com.kaaterskil.workflow.bpm.common.event.MessageEventDefinition;
import com.kaaterskil.workflow.bpm.common.event.StartEvent;
import com.kaaterskil.workflow.bpm.common.process.Process;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventFactory;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventType;
import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.ProcessDefinitionEntity;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.runtime.ProcessInstance;

public class ProcessInstanceUtil {

    public static ProcessInstance createAndStartProcessInstance(
            ProcessDefinitionEntity processDefinition, Map<String, Object> variables) {

        final Process process = ProcessDefinitionUtil.getProcess(processDefinition.getId());
        if (process == null) {
            final String message = String.format(
                    "Cannot start process instance. Process definition {} (id {}) cannot be found",
                    processDefinition.getName(), processDefinition.getId());
            throw new WorkflowException(message);
        }

        final FlowElement initialFlowElement = process.getInitialFlowElement();
        if (initialFlowElement == null) {
            final String message = String.format("No start element found for process definition {}",
                    processDefinition.getId());
            throw new WorkflowException(message);
        }

        return createAndStartProcessInstance(processDefinition, initialFlowElement, process,
                variables);
    }

    private static ProcessInstance createAndStartProcessInstance(
            ProcessDefinitionEntity processDefinition, FlowElement initialFlowElement,
            Process process, Map<String, Object> variables) {

        final CommandContext commandContext = Context.getCommandContext();
        final Token processInstance = commandContext.getTokenService()
                .createProcessInstanceToken(processDefinition);

        commandContext.getProcessInstanceService().recordProcessInstanceStart(processInstance,
                initialFlowElement);

        if (variables != null && !variables.isEmpty()) {
            for (final String variableName : variables.keySet()) {
                processInstance.setVariable(variableName, variables.get(variableName));
            }
        }

        commandContext.getProcessEngineService().getEventDispatcher()
                .dispatchEvent(WorkflowEventFactory.createEntityWithVariablesEvent(
                        WorkflowEventType.ENTITY_INITIALIZED, processInstance, variables));

        // Create a child token to execute all the elements
        final Token token = commandContext.getTokenService().createChildToken(processInstance);
        token.setCurrentFlowElement(initialFlowElement);

        // Handle event subprocess
        for (final FlowElement flowElement : process.getFlowElements()) {
            if (flowElement instanceof EventSubProcess) {
                final EventSubProcess eventSubProcess = (EventSubProcess) flowElement;
                for (final FlowElement subElement : eventSubProcess.getFlowElements()) {
                    if (subElement instanceof StartEvent) {
                        final StartEvent startEvent = (StartEvent) subElement;
                        if (!startEvent.getEventDefinitions().isEmpty()) {
                            final EventDefinition eventDefinition = startEvent.getEventDefinitions()
                                    .get(0);
                            if (eventDefinition instanceof MessageEventDefinition) {
                                final MessageEventDefinition messageEventDefinition = (MessageEventDefinition) eventDefinition;

                                final Token messageToken = commandContext.getTokenService()
                                        .createChildToken(processInstance);
                                messageToken.setCurrentFlowElement(startEvent);
                                messageToken.setEventScope(true);

                                commandContext.getEventSubscriptionService()
                                        .saveMessageEvent(messageEventDefinition, messageToken);
                            }
                        }
                    }
                }
            }
        }

        startProcessInstance(processInstance, commandContext, variables);

        return processInstance;
    }

    public static void startProcessInstance(Token processInstance, CommandContext commandContext,
            Map<String, Object> variables) {
        final Token token = processInstance.getChildTokens().get(0);
        commandContext.getWorkflow().addContinueProcessOperation(token);

        commandContext.getProcessEngineService().getEventDispatcher()
                .dispatchEvent(WorkflowEventFactory.createProcessStartedEvent(token, variables));
    }
}
