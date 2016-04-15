package com.kaaterskil.workflow.engine.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kaaterskil.workflow.engine.persistence.entity.EventSubscription;
import com.kaaterskil.workflow.engine.persistence.entity.EventSubscriptionType;
import com.kaaterskil.workflow.engine.persistence.entity.MessageEventSubscription;

@Repository
public interface EventSubscriptionRepository extends JpaRepository<EventSubscription, Long> {

    @Query(value = "select e from EventSubscription e where e.token.id = :tokenId")
    List<EventSubscription> findByTokenId(@Param("tokenId") Long tokenId);

    @Query(value = "select e from EventSubscription e where e.eventType = :eventType and e.token.id = :tokenId")
    List<EventSubscription> findByTokenIdAndEventType(@Param("tokenId") Long tokenId,
            @Param("eventType") EventSubscriptionType eventType);

    @Query(value = "select e from EventSubscription e where e.eventType = 'MESSAGE' "
            + "and e.eventName = :eventName and e.processInstanceId = :processInstanceId")
    List<MessageEventSubscription> findMessageEventSubscriptionsByProcessInstanceAndEventName(
            @Param("processInstanceId") Long processInstanceId,
            @Param("eventName") String messageName);
}
