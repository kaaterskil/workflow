package com.kaaterskil.workflow.engine.interceptor;

import com.kaaterskil.workflow.engine.command.Command;

public interface CommandExecutor {

    <T> T execute(Command<T> command);
}
