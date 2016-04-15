package com.kaaterskil.workflow.engine.event;

import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventFactory;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventType;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.EventSubscription;
import com.kaaterskil.workflow.engine.persistence.entity.EventSubscriptionType;

public class MessageEventHandler extends AbstractEventHandler {

    @Override
    public EventSubscriptionType getEventHandlerType() {
        return EventSubscriptionType.MESSAGE;
    }

    @Override
    public void handleEvent(EventSubscription eventSubscription, Object payload,
            CommandContext commandContext) {

        commandContext.getProcessEngineService().getEventDispatcher()
                .dispatchEvent(WorkflowEventFactory.createMessageEvent(
                        WorkflowEventType.ACTIVITY_MESSAGE_RECEIVED,
                        eventSubscription.getActivityId(), eventSubscription.getEventName(),
                        payload, eventSubscription.getToken().getId(),
                        eventSubscription.getProcessInstanceId(),
                        eventSubscription.getToken().getProcessDefinitionId()));

        super.handleEvent(eventSubscription, payload, commandContext);
    }

}
