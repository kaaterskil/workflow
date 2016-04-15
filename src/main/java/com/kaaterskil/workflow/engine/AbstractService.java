package com.kaaterskil.workflow.engine;

import com.kaaterskil.workflow.engine.interceptor.CommandExecutor;

public abstract class AbstractService {

    protected CommandExecutor commandExecutor;

    public CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }

    public void setCommandExecutor(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }
}
