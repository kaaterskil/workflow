package com.kaaterskil.workflow.engine.command;

import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventListener;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventType;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;

public class AddEventListenerCommand implements Command<Void> {

    private final WorkflowEventListener listener;
    private WorkflowEventType type;

    public AddEventListenerCommand(WorkflowEventListener listener) {
        this.listener = listener;
    }

    public AddEventListenerCommand(WorkflowEventListener listener, WorkflowEventType type) {
        this.listener = listener;
        this.type = type;
    }

    @Override
    public Void execute(CommandContext commandContext) {
        if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null");
        }

        if (type != null) {
            commandContext.getProcessEngineService().getEventDispatcher()
                    .addTypedEventListener(listener, type);
        } else {
            commandContext.getProcessEngineService().getEventDispatcher()
                    .addEventListener(listener);
        }

        return null;
    }
}
