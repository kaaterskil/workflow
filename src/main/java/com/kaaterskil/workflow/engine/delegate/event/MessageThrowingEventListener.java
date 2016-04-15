package com.kaaterskil.workflow.engine.delegate.event;

import java.util.List;

import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.persistence.entity.MessageEventSubscription;
import com.kaaterskil.workflow.engine.service.EventSubscriptionService;

public class MessageThrowingEventListener extends AbstractDelegateEventListener {

    protected String messageName;

    @Override
    public void onEvent(WorkflowEvent event) {
        if (isValid(event)) {
            if (event.getProcessInstanceId() == null) {
                throw new IllegalStateException("This event is not part of an ongoing process");
            }

            final EventSubscriptionService service = Context.getCommandContext()
                    .getEventSubscriptionService();
            final List<MessageEventSubscription> subscriptions = service
                    .findMessageEventSubscriptionsByProcessInstanceAndEventName(
                            event.getProcessInstanceId(), messageName);

            if (subscriptions != null && !subscriptions.isEmpty()) {
                for (final MessageEventSubscription each : subscriptions) {
                    service.eventReceived(each, null, false);
                }
            }
        }
    }

    @Override
    public boolean isFailOnException() {
        return true;
    }

}
