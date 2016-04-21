package com.kaaterskil.workflow.engine.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.engine.command.Command;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.operation.AbstractOperation;

public class CommandInvoker extends CommandInterceptor {
    private static final Logger log = LoggerFactory.getLogger(CommandInvoker.class);

    @Override
    @SuppressWarnings("unchecked")
    public <T> T execute(final Command<T> command) {
        final CommandContext commandContext = Context.getCommandContext();

        commandContext.getWorkflow().addOperation(new Runnable() {
            @Override
            public void run() {
                commandContext.setResult(command.execute(commandContext));
            }
        });

        executeOperations(commandContext);

        if(commandContext.hasTokens()) {
            commandContext.getWorkflow();
            executeOperations(commandContext);
        }

        return (T) commandContext.getResult();
    }

    @Override
    public CommandInterceptor getNext() {
        return null;
    }

    @Override
    public void setNext(CommandExecutor next) {
        throw new UnsupportedOperationException(
                "CommandInvoker must be the last interceptor in the chain");
    }

    protected void executeOperations(CommandContext commandContext) {
        while (!commandContext.getWorkflow().isEmpty()) {
            final Runnable runnable = commandContext.getWorkflow().getNextOperation();
            executeOperation(runnable);
        }
    }

    public void executeOperation(Runnable runnable) {
        if (runnable instanceof AbstractOperation) {
            final AbstractOperation operation = (AbstractOperation) runnable;

            if (operation.getToken() == null || !operation.getToken().isEnded()) {
                log.debug("Executing operation " + operation.getClass().getSimpleName());
                runnable.run();
            }
        } else {
            runnable.run();
        }
    }
}
