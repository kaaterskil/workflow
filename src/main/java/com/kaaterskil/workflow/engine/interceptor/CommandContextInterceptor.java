package com.kaaterskil.workflow.engine.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.engine.ProcessEngineService;
import com.kaaterskil.workflow.engine.command.Command;
import com.kaaterskil.workflow.engine.context.Context;

public class CommandContextInterceptor extends CommandInterceptor {
    private static final Logger log = LoggerFactory.getLogger(CommandContextInterceptor.class);

    protected CommandContextFactory commandContextFactory;
    protected ProcessEngineService processEngineService;

    public CommandContextInterceptor(CommandContextFactory commandContextFactory,
            ProcessEngineService processEngineService) {
        this.commandContextFactory = commandContextFactory;
        this.processEngineService = processEngineService;
    }

    @Override
    public <T> T execute(Command<T> command) {
//        final CommandInvocationContext commandInvocationContext = new CommandInvocationContext(
//                command);
//        Context.setCommandInvocationContext(commandInvocationContext);

        try {
            // TODO handle transactions, exceptions and compensation
            CommandContext commandContext = Context.getCommandContext();
            if (commandContext == null) {
                commandContext = commandContextFactory.createInstance();
            } else {
                log.debug("Valid context found. Reusing it for the current command");
            }

            // Push onto the stack
            Context.setCommandContext(commandContext);
            Context.setProcessEngineService(processEngineService);

            return next.execute(command);

        } finally {
            // Pop from the stack
            Context.removeCommandContext();
//            Context.removeCommandInvocationContext();
            Context.removeProcessEngineService();
        }
    }

    /*---------- Getter/Setters ----------*/

    public CommandContextFactory getCommandContextFactory() {
        return commandContextFactory;
    }

    public void setCommandContextFactory(CommandContextFactory commandContextFactory) {
        this.commandContextFactory = commandContextFactory;
    }

    public ProcessEngineService getProcessEngineService() {
        return processEngineService;
    }

    public void setProcessEngineService(ProcessEngineService processEngineService) {
        this.processEngineService = processEngineService;
    }

}
