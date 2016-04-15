package com.kaaterskil.workflow.engine.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventFactory;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventType;
import com.kaaterskil.workflow.engine.persistence.entity.ProcessInstanceEntity;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.persistence.repository.ProcessInstanceRepository;
import com.kaaterskil.workflow.engine.service.ProcessInstanceService;

@Component
public class ProcessInstanceServiceImpl implements ProcessInstanceService {

    @Autowired
    private ProcessInstanceRepository repository;

    @Override
    public void recordProcessInstanceStart(Token token, FlowElement startElement) {
        ProcessInstanceEntity processInstance = create(token);
        processInstance.setStartActivityId(startElement.getId());

        processInstance = save(processInstance);

        Context.getCommandContext().getEventDispatcher()
                .dispatchEvent(WorkflowEventFactory.createEntityEvent(
                        WorkflowEventType.HISTORIC_PROCESS_INSTANCE_CREATED, processInstance));
    }

    @Override
    public ProcessInstanceEntity create() {
        return new ProcessInstanceEntity();
    }

    @Override
    public ProcessInstanceEntity create(Token processInstanceToken) {
        return new ProcessInstanceEntity(processInstanceToken);
    }

    @Override
    public ProcessInstanceEntity save(ProcessInstanceEntity processInstance) {
        return repository.save(processInstance);
    }
}
