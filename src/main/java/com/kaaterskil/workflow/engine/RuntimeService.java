package com.kaaterskil.workflow.engine;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.kaaterskil.workflow.engine.command.AddEventListenerCommand;
import com.kaaterskil.workflow.engine.command.DispatchEventCommand;
import com.kaaterskil.workflow.engine.command.StartProcessInstanceCommand;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEvent;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventListener;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventType;
import com.kaaterskil.workflow.engine.runtime.ProcessInstance;

@Component
public class RuntimeService extends AbstractService {

    public ProcessInstance startProcessInstanceByKey(String processDefinitionKey) {
        return commandExecutor
                .execute(new StartProcessInstanceCommand(processDefinitionKey, null, null));
    }

    public ProcessInstance startProcessInstanceByKey(String processDefinitionKey,
            Map<String, Object> variables) {
        return commandExecutor
                .execute(new StartProcessInstanceCommand(processDefinitionKey, null, variables));
    }

    public void addEventListener(WorkflowEventListener listener) {
        commandExecutor.execute(new AddEventListenerCommand(listener));
    }

    public void addEventListener(WorkflowEventListener listener, WorkflowEventType... eventTypes) {
        commandExecutor.execute(new AddEventListenerCommand(listener, eventTypes));
    }

    public void dispatchEvent(WorkflowEvent event) {
        commandExecutor.execute(new DispatchEventCommand(event));
    }
}