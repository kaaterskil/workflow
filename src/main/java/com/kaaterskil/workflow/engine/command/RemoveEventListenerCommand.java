package com.kaaterskil.workflow.engine.command;

import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventListener;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;

public class RemoveEventListenerCommand implements Command<Void> {

    private final WorkflowEventListener listener;

    public RemoveEventListenerCommand(WorkflowEventListener listener) {
        this.listener = listener;
    }

    @Override
    public Void execute(CommandContext commandContext) {
        if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null");
        }

        commandContext.getProcessEngineService().getEventDispatcher().removeEventListener(listener);

        return null;
    }
}
