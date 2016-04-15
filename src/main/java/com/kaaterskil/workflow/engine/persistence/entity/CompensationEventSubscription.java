package com.kaaterskil.workflow.engine.persistence.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "COMPENSATE")
public class CompensationEventSubscription extends EventSubscription {

    public CompensationEventSubscription() {
        eventType = EventSubscriptionType.COMPENSATE;
    }

}
