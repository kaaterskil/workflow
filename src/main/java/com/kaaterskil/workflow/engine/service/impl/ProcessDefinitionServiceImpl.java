package com.kaaterskil.workflow.engine.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kaaterskil.workflow.engine.persistence.entity.ProcessDefinitionEntity;
import com.kaaterskil.workflow.engine.persistence.repository.ProcessDefinitionRepository;
import com.kaaterskil.workflow.engine.service.ProcessDefinitionService;

@Component
public class ProcessDefinitionServiceImpl implements ProcessDefinitionService {

    @Autowired
    private ProcessDefinitionRepository repository;

    @Override
    public ProcessDefinitionEntity create() {
        return new ProcessDefinitionEntity();
    }

    @Override
    public ProcessDefinitionEntity save(ProcessDefinitionEntity processDefinition) {
        return repository.save(processDefinition);
    }
}
