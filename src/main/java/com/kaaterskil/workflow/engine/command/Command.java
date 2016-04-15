package com.kaaterskil.workflow.engine.command;

import com.kaaterskil.workflow.engine.interceptor.CommandContext;

public interface Command<T> {

    T execute(CommandContext commandContext);
}
