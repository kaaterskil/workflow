package com.kaaterskil.workflow.engine.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kaaterskil.workflow.bpm.common.event.MessageEventDefinition;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventFactory;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventType;
import com.kaaterskil.workflow.engine.event.EventHandler;
import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.engine.executor.JobHandler;
import com.kaaterskil.workflow.engine.persistence.entity.CompensationEventSubscription;
import com.kaaterskil.workflow.engine.persistence.entity.EventSubscription;
import com.kaaterskil.workflow.engine.persistence.entity.EventSubscriptionType;
import com.kaaterskil.workflow.engine.persistence.entity.MessageEntity;
import com.kaaterskil.workflow.engine.persistence.entity.MessageEventSubscription;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.persistence.repository.EventSubscriptionRepository;
import com.kaaterskil.workflow.engine.service.EventSubscriptionService;
import com.kaaterskil.workflow.engine.service.JobService;
import com.kaaterskil.workflow.util.CollectionUtil;

@Component
public class EventSubscriptionServiceImpl implements EventSubscriptionService {

    @Autowired
    EventSubscriptionRepository repository;

    @Override
    public void eventReceived(EventSubscription eventSubscription, Object payload,
            boolean isAsync) {
        if (isAsync) {
            processAsynchronousEvent(eventSubscription, payload);
        } else {
            processSynchronousEvent(eventSubscription, payload);
        }
    }

    private void processAsynchronousEvent(EventSubscription eventSubscription, Object payload) {
        final JobService jobService = Context.getCommandContext().getJobService();

        final MessageEntity message = jobService.createMessage();
        message.setJobHandlerType(JobHandler.EVENT_TYPE);
        message.setJobHandlerConfiguration(eventSubscription.getId());

        final Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.SECOND,
                Context.getProcessEngineService().getLockTimeAsyncJobWaitTimeSeconds());
        message.setLockExpiresAt(c.getTime());

        jobService.send(message);
    }

    private void processSynchronousEvent(EventSubscription eventSubscription, Object payload) {
        if (eventSubscription instanceof CompensationEventSubscription) {
            delete(eventSubscription);
        }

        final EventHandler eventHandler = Context.getProcessEngineService()
                .getEventHandler(eventSubscription.getEventType());
        if (eventHandler == null) {
            throw new WorkflowException(
                    "Could not find event handler of type " + eventSubscription.getEventType());
        }
        eventHandler.handleEvent(eventSubscription, payload, Context.getCommandContext());
    }

    @Override
    public EventSubscription findById(Long id) {
        return repository.findOne(id);
    }

    @Override
    public List<EventSubscription> findByToken(Token token) {
        return repository.findByTokenId(token.getId());
    }

    @Override
    public List<EventSubscription> findByTokenAndEventType(Token token,
            EventSubscriptionType eventType) {
        return repository.findByTokenIdAndEventType(token.getId(), eventType);
    }

    @Override
    public List<CompensationEventSubscription> findCompensationEventSubscriptionsByToken(
            Token token) {
        final Long tokenId = token.getId();
        final EventSubscriptionType eventType = EventSubscriptionType.COMPENSATE;
        final List<EventSubscription> eventSubscriptions = repository
                .findByTokenIdAndEventType(tokenId, eventType);

        final List<CompensationEventSubscription> result = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(eventSubscriptions)) {
            for (final EventSubscription each : eventSubscriptions) {
                if (each instanceof CompensationEventSubscription) {
                    result.add((CompensationEventSubscription) each);
                }
            }
        }
        return result;
    }

    @Override
    public List<MessageEventSubscription> findMessageEventSubscriptionsByProcessInstanceAndEventName(
            Long processInstanceId, String messageName) {
        return repository.findMessageEventSubscriptionsByProcessInstanceAndEventName(
                processInstanceId, messageName);
    }

    @Override
    public MessageEventSubscription createMessageEventSubscription() {
        return new MessageEventSubscription();
    }

    @Override
    public CompensationEventSubscription createCompensationEventSubscription() {
        return new CompensationEventSubscription();
    }

    @Override
    public CompensationEventSubscription insertCompensationEvent(Token token, String activityId) {
        final CompensationEventSubscription subscription = createCompensationEventSubscription();
        subscription.setToken(token);
        subscription.setActivityId(activityId);

        final CompensationEventSubscription savedSubscription = (CompensationEventSubscription) save(
                subscription);
        return savedSubscription;
    }

    @Override
    public EventSubscription save(EventSubscription eventSubscription) {
        return repository.save(eventSubscription);
    }

    @Override
    public MessageEventSubscription saveMessageEvent(MessageEventDefinition messageEventDefinition,
            Token token) {
        final MessageEventSubscription subscription = createMessageEventSubscription();
        subscription.setToken(token);
        subscription.setEventName(messageEventDefinition.getMessageRef());
        subscription.setActivityId(token.getCurrentActivityId());
        subscription.setProcessDefinitionId(token.getProcessDefinitionId());

        final MessageEventSubscription savedSubscription = (MessageEventSubscription) save(
                subscription);
        token.getEventSubscriptions().add(savedSubscription);

        return savedSubscription;
    }

    @Override
    public void delete(EventSubscription eventSubscription) {
        repository.delete(eventSubscription);

        Context.getCommandContext().getEventDispatcher().dispatchEvent(WorkflowEventFactory
                .createEntityEvent(WorkflowEventType.ENTITY_DELETED, eventSubscription));
    }
}
