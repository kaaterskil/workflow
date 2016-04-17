package com.kaaterskil.workflow.engine.command;

import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventListener;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventType;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;

public class AddEventListenerCommand implements Command<Void> {

    private final WorkflowEventListener listener;
    private WorkflowEventType[] types;

    public AddEventListenerCommand(WorkflowEventListener listener) {
        this.listener = listener;
    }

    public AddEventListenerCommand(WorkflowEventListener listener, WorkflowEventType ... types) {
        this.listener = listener;
        this.types = types;
    }

    @Override
    public Void execute(CommandContext commandContext) {
        if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null");
        }

        if (types != null) {
            commandContext.getProcessEngineService().getEventDispatcher()
                    .addEventListener(listener, types);
        } else {
            commandContext.getProcessEngineService().getEventDispatcher()
                    .addEventListener(listener);
        }

        return null;
    }
}
