package com.kaaterskil.workflow.engine.service;

import java.util.List;

import com.kaaterskil.workflow.bpm.common.event.MessageEventDefinition;
import com.kaaterskil.workflow.engine.persistence.entity.CompensateEventSubscription;
import com.kaaterskil.workflow.engine.persistence.entity.EventSubscription;
import com.kaaterskil.workflow.engine.persistence.entity.EventSubscriptionType;
import com.kaaterskil.workflow.engine.persistence.entity.MessageEventSubscription;
import com.kaaterskil.workflow.engine.persistence.entity.Token;

public interface EventSubscriptionService {

    void eventReceived(EventSubscription eventSubscription, Object payload, boolean isAsync);

    EventSubscription findById(Long configuration);

    List<EventSubscription> findByToken(Token token);

    List<EventSubscription> findByTokenAndEventType(Token token, EventSubscriptionType eventType);

    List<CompensateEventSubscription> findCompensationEventSubscriptionsByToken(Token token);

    List<MessageEventSubscription> findMessageEventSubscriptionsByProcessInstanceAndEventName(
            Long processInstanceId, String messageName);

    MessageEventSubscription createMessageEventSubscription();

    CompensateEventSubscription createCompensationEventSubscription();

    CompensateEventSubscription insertCompensationEvent(Token token, String activityId);

    EventSubscription save(EventSubscription eventSubscription);

    MessageEventSubscription saveMessageEvent(MessageEventDefinition messageEventDefinition, Token token);

    void delete(EventSubscription eventSubscription);
}
