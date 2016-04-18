package com.kaaterskil.workflow.engine.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventFactory;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventType;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.persistence.entity.VariableEntity;
import com.kaaterskil.workflow.engine.persistence.repository.VariableRepository;
import com.kaaterskil.workflow.engine.service.VariableService;
import com.kaaterskil.workflow.engine.variable.VariableType;

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
    public VariableEntity create() {
        return new VariableEntity();
    }

    @Override
    public VariableEntity create(String name, VariableType type, Object value) {
        final VariableEntity entity = create();
        entity.setName(name);
        entity.setType(type);
        entity.setValueType(type.getType());
        entity.setValue(value);

        final Date now = new Date();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        return entity;
    }

    @Override
    public VariableEntity createAndInsert(String variableName, VariableType type, Object value, Token token) {
        final VariableEntity variable = create(variableName, type, value);
        variable.setTokenId(token.getId());
        variable.setProcessInstanceId(token.getProcessInstanceId());

        return save(variable);
    }

    @Override
    public VariableEntity save(VariableEntity variable) {
        return repository.save(variable);
    }

    @Override
    public void delete(VariableEntity variable) {
        repository.delete(variable);

        Context.getCommandContext().getEventDispatcher().dispatchEvent(
                WorkflowEventFactory.createEntityEvent(WorkflowEventType.ENTITY_DELETED, variable));
    }

}
