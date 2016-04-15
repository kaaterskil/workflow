package com.kaaterskil.workflow.engine.interceptor;

import com.kaaterskil.workflow.engine.command.Command;

public class CommandInvocationContext {

    protected Throwable throwable;
    protected final Command<?> command;

    public CommandInvocationContext(Command<?> command) {
        this.command = command;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Command<?> getCommand() {
        return command;
    }
}
