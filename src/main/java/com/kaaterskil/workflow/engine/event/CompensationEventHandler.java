package com.kaaterskil.workflow.engine.event;

import java.util.List;

import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.bpm.common.activity.SubProcess;
import com.kaaterskil.workflow.bpm.common.process.Process;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventFactory;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventType;
import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.CompensateEventSubscription;
import com.kaaterskil.workflow.engine.persistence.entity.EventSubscription;
import com.kaaterskil.workflow.engine.persistence.entity.EventSubscriptionType;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.util.ProcessDefinitionUtil;
import com.kaaterskil.workflow.engine.util.ScopeUtil;

public class CompensationEventHandler implements EventHandler {

    @Override
    public EventSubscriptionType getEventHandlerType() {
        return EventSubscriptionType.COMPENSATE;
    }

    @Override
    public void handleEvent(EventSubscription eventSubscription, Object payload,
            CommandContext commandContext) {

        final String configuration = eventSubscription.getConfiguration();
        if (configuration == null) {
            throw new WorkflowException(
                    "compensating token not set for compensate event subscription with id "
                            + eventSubscription.getId());
        }

        final Long tokenId = Long.valueOf(configuration);
        final Token compensatingToken = commandContext.getTokenService().findById(tokenId);

        final Long processDefinitionId = compensatingToken.getProcessDefinitionId();
        final Process process = ProcessDefinitionUtil.getProcess(processDefinitionId);
        if (process == null) {
            throw new WorkflowException("cannot start process instance. Model "
                    + processDefinitionId + " could not be found.");
        }

        final FlowElement flowElement = process.getFlowElement(eventSubscription.getActivityId(),
                true);
        if (flowElement instanceof SubProcess
                && ((SubProcess) flowElement).isForCompensation() == false) {
            final List<CompensateEventSubscription> events = commandContext
                    .getEventSubscriptionService()
                    .findCompensationEventSubscriptionsByToken(compensatingToken);
            ScopeUtil.throwCompensationEvent(events, compensatingToken, false);
        } else {
            try {
                Context.getCommandContext().getEventDispatcher()
                        .dispatchEvent(WorkflowEventFactory.createActivityEvent(
                                WorkflowEventType.ACTIVITY_COMPENSATE, flowElement.getId(),
                                flowElement.getName(), compensatingToken, flowElement));

                compensatingToken.setCurrentFlowElement(flowElement);
                Context.getWorkflow().addContinueProcessInCompensation(compensatingToken);
            } catch (final Exception e) {
                throw new WorkflowException(
                        "error while handling compensation event " + eventSubscription, e);
            }
        }
    }
}
