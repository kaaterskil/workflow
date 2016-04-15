package com.kaaterskil.workflow.engine.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventFactory;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventType;
import com.kaaterskil.workflow.engine.persistence.entity.VariableEntity;
import com.kaaterskil.workflow.engine.persistence.repository.VariableRepository;
import com.kaaterskil.workflow.engine.service.VariableService;

@Component
public class VariableServiceImpl implements VariableService {

    @Autowired
    private VariableRepository repository;

    @Override
    public List<VariableEntity> findByTokenId(Long tokenId) {
        return repository.findByTokenId(tokenId);
    }

    @Override
    public VariableEntity findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public void delete(VariableEntity variable) {
        repository.delete(variable);

        Context.getCommandContext().getEventDispatcher().dispatchEvent(
                WorkflowEventFactory.createEntityEvent(WorkflowEventType.ENTITY_DELETED, variable));
    }

}
