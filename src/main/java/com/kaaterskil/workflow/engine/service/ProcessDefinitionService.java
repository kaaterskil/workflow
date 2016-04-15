package com.kaaterskil.workflow.engine.service;

import com.kaaterskil.workflow.engine.persistence.entity.ProcessDefinitionEntity;

public interface ProcessDefinitionService {

    ProcessDefinitionEntity create();

    ProcessDefinitionEntity save(ProcessDefinitionEntity processDefinition);
}
