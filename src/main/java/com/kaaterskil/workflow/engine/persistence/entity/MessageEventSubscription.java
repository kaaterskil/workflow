package com.kaaterskil.workflow.engine.persistence.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "MESSAGE")
public class MessageEventSubscription extends EventSubscription {

    public MessageEventSubscription() {
        eventType = EventSubscriptionType.MESSAGE;
    }
}
