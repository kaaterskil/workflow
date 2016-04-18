package com.kaaterskil.workflow.engine.service;

import java.util.List;

import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.persistence.entity.VariableEntity;
import com.kaaterskil.workflow.engine.variable.VariableType;

public interface VariableService {

    List<VariableEntity> findByTokenId(Long tokenId);

    VariableEntity findByName(String name);

    VariableEntity create();

    VariableEntity create(String name, VariableType type, Object value);

    VariableEntity createAndInsert(String name, VariableType type, Object value, Token token);

    VariableEntity save(VariableEntity variable);

    void delete(VariableEntity variable);
}
