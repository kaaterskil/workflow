package com.kaaterskil.workflow.engine.command;

import com.kaaterskil.workflow.engine.delegate.event.WorkflowEvent;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;

public class DispatchEventCommand implements Command<Void> {

    private final WorkflowEvent event;

    public DispatchEventCommand(WorkflowEvent event) {
        this.event = event;
    }

    @Override
    public Void execute(CommandContext commandContext) {
        if(event == null) {
            throw new IllegalArgumentException("event cannot be null");
        }

        commandContext.getEventDispatcher().dispatchEvent(event);

        return null;
    }
}
