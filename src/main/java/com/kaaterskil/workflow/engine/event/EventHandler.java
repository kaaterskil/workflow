package com.kaaterskil.workflow.engine.event;

import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.EventSubscription;
import com.kaaterskil.workflow.engine.persistence.entity.EventSubscriptionType;

public interface EventHandler {

    EventSubscriptionType getEventHandlerType();

    void handleEvent(EventSubscription eventSubscription, Object payload,
            CommandContext commandContext);
}
