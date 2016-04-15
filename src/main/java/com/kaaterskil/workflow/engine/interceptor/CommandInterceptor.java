package com.kaaterskil.workflow.engine.interceptor;

public abstract class CommandInterceptor implements CommandExecutor {

    protected CommandExecutor next;

    public CommandExecutor getNext() {
        return next;
    }

    public void setNext(CommandExecutor next) {
        this.next = next;
    }

}
