package com.kaaterskil.workflow.engine.delegate.event;

import org.springframework.stereotype.Component;

import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.ProcessDefinitionEntity;
import com.kaaterskil.workflow.engine.util.ProcessDefinitionUtil;

@Component
public class WorkflowEventDispatcherImpl implements WorkflowEventDispatcher {

    protected WorkflowEventUtil eventHelper;

    public WorkflowEventDispatcherImpl() {
        eventHelper = new WorkflowEventUtil();
    }

    @Override
    public void addEventListener(WorkflowEventListener listener) {
        eventHelper.addEventListener(listener);
    }

    @Override
    public void addEventListener(WorkflowEventListener listener, WorkflowEventType... eventTypes) {
        eventHelper.addEventListener(listener, eventTypes);
    }

    @Override
    public void addTypedEventListener(WorkflowEventListener listener, WorkflowEventType type) {
        eventHelper.addTypedEventListener(listener, type);
    }

    @Override
    public void removeEventListener(WorkflowEventListener listener) {
        eventHelper.removeEventListener(listener);
    }

    @Override
    public void dispatchEvent(WorkflowEvent event) {
        // Dispatch event from the process engine service
        eventHelper.dispatchEvent(event);

        // Dispatch event from the process definition
        final CommandContext commandContext = Context.getCommandContext();
        if (commandContext != null) {
            final ProcessDefinitionEntity processDefinition = findProcessDefinition(event);
            if (processDefinition != null) {
                processDefinition.getEventHelper().dispatchEvent(event);
            }
        }
    }

    private ProcessDefinitionEntity findProcessDefinition(WorkflowEvent event) {
        ProcessDefinitionEntity processDefinition = null;

        if (event instanceof EntityWorkflowEvent) {
            final EntityWorkflowEvent entityEvent = (EntityWorkflowEvent) event;
            final Object obj = entityEvent.getEntity();
            if (obj instanceof ProcessDefinitionEntity) {
                processDefinition = (ProcessDefinitionEntity) obj;
            }
        }

        if (processDefinition == null && event.getProcessDefinitionId() != null) {
            processDefinition = ProcessDefinitionUtil
                    .getProcessDefinition(event.getProcessDefinitionId());
        }
        return processDefinition;
    }
}
