package com.kaaterskil.workflow.engine.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventFactory;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventType;
import com.kaaterskil.workflow.engine.persistence.entity.ActivityEntity;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.persistence.repository.ActivityRepository;
import com.kaaterskil.workflow.engine.service.ActivityService;

@Component
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository repository;

    @Override
    public void recordActivityEnd(Token token) {
        final ActivityEntity activityInstance = findActivityEntity(token, false);
        if (activityInstance != null) {
            activityInstance.markEnded();
            repository.save(activityInstance);

            Context.getCommandContext().getEventDispatcher()
                    .dispatchEvent(WorkflowEventFactory.createEntityEvent(
                            WorkflowEventType.HISTORIC_ACTIVITY_INSTANCE_ENDED, activityInstance));
        }
    }

    @Override
    public ActivityEntity findActivityEntity(Token token, boolean createIfNotFound) {
        final Long tokenId = token.getId();
        final String activityId = token.getCurrentActivity();
        List<ActivityEntity> instances = repository.findUnfinishedActivities(tokenId, activityId);

        if (instances != null && !instances.isEmpty()) {
            return instances.get(0);
        }

        if (token.getParent() != null) {
            instances = repository.findUnfinishedActivities(token.getParent().getId(), activityId);
            if (instances != null & !instances.isEmpty()) {
                return instances.get(0);
            }
        }

        if (createIfNotFound && activityId != null
                && ((token.getCurrentFlowElement() != null
                        && token.getCurrentFlowElement() instanceof FlowNode)
                        || token.getCurrentFlowElement() == null)) {
            return create(token);
        }
        return null;
    }

    @Override
    public ActivityEntity create() {
        return new ActivityEntity();
    }

    private ActivityEntity create(Token token) {
        final Long processDefinitionId = token.getProcessDefinitionId();
        final Long processInstanceId = token.getProcessInstanceId();

        final ActivityEntity instance = create();
        instance.setProcessDefinitionId(processDefinitionId);
        instance.setProcessInstanceId(processInstanceId);
        instance.setTokenId(token.getId());
        instance.setKey(token.getCurrentActivity());
        if (token.getCurrentFlowElement() != null) {
            instance.setName(token.getCurrentFlowElement().getName());
            instance.setType(token.getCurrentFlowElement().getClass().getSimpleName());
        }

        final Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        instance.setStartedAt(c.getTime());

        final ActivityEntity savedInstance = repository.save(instance);

        return savedInstance;
    }

}
