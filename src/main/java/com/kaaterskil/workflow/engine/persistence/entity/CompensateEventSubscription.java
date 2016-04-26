package com.kaaterskil.workflow.engine.persistence.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "COMPENSATE")
public class CompensateEventSubscription extends EventSubscription {

    public CompensateEventSubscription() {
        eventType = EventSubscriptionType.COMPENSATE;
    }

}
