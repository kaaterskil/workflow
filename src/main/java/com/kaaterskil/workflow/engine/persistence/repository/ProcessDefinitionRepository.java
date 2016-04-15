package com.kaaterskil.workflow.engine.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kaaterskil.workflow.engine.persistence.entity.ProcessDefinitionEntity;

@Repository
public interface ProcessDefinitionRepository
        extends JpaRepository<ProcessDefinitionEntity, Long> {

    ProcessDefinitionEntity findByKey(String key);
}
