package com.kaaterskil.workflow.engine.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kaaterskil.workflow.engine.persistence.entity.ProcessInstanceEntity;

@Repository
public interface ProcessInstanceRepository
        extends JpaRepository<ProcessInstanceEntity, Long> {

}
