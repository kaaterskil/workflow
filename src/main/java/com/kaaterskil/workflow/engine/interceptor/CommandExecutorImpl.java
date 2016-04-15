package com.kaaterskil.workflow.engine.interceptor;

import com.kaaterskil.workflow.engine.command.Command;
import com.kaaterskil.workflow.engine.context.Context;

public class CommandExecutorImpl extends CommandInterceptor {

    @Override
    public <T> T execute(Command<T> command) {
        return command.execute(Context.getCommandContext());
    }

}
