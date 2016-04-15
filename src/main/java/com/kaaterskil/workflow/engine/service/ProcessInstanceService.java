package com.kaaterskil.workflow.engine.service;

import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.engine.persistence.entity.ProcessInstanceEntity;
import com.kaaterskil.workflow.engine.persistence.entity.Token;

public interface ProcessInstanceService {

    void recordProcessInstanceStart(Token processInstance, FlowElement startElement);

    ProcessInstanceEntity create();

    ProcessInstanceEntity create(Token processInstanceToken);

    ProcessInstanceEntity save(ProcessInstanceEntity processInstance);
}
