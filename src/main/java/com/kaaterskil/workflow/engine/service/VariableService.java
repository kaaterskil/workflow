package com.kaaterskil.workflow.engine.service;

import java.util.List;

import com.kaaterskil.workflow.engine.persistence.entity.VariableEntity;

public interface VariableService {

    List<VariableEntity> findByTokenId(Long tokenId);

    VariableEntity findByName(String name);

    void delete(VariableEntity variable);
}
