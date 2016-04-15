package com.kaaterskil.workflow.engine.operation;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.Token;

public class Workflow {
    private static final Logger log = LoggerFactory.getLogger(Workflow.class);

    private CommandContext commandContext;
    private final LinkedList<Runnable> operations = new LinkedList<>();

    public Workflow(CommandContext commandContext) {
        this.commandContext = commandContext;
    }

    public boolean isEmpty() {
        return operations.isEmpty();
    }

    public Runnable getNextOperation() {
        return operations.poll();
    }

    public void addOperation(Runnable operation) {
        addOperation(operation, null);
    }

    public void addOperation(Runnable operation, Token token) {
        operations.add(operation);
        log.debug("Operation {} added to workflow", operation);

        if (token != null) {
            commandContext.addToken(token);
        }
    }

    /*---------- Specific Operations ----------*/

    public void addContinueProcessOperation(Token token) {
        addOperation(new ContinueProcessOperation(commandContext, token), token);
    }

    public void addContinueProcessSynchronousExecution(Token token) {
        addOperation(new ContinueProcessOperation(commandContext, token), token);
    }

    public void addContinueProcessInCompensation(Token token) {
        addOperation(new ContinueProcessOperation(commandContext, token, false, true), token);
    }

    public void addOutgoingSequenceFlow(Token token, boolean checkConditions) {
        addOperation(new OutgoingSequenceFlowOperation(commandContext, token, checkConditions), token);
    }

    public void addEndTokenOperation(Token token) {
        addOperation(new EndTokenOperation(commandContext, token), token);
    }

    public void addDestroyScopeOperation(Token token) {
        addOperation(new DestroyScopeOperation(commandContext, token), token);
    }

    public void addTriggerTokenOperation(Token token) {
        addOperation(new TriggerTokenOperation(commandContext, token), token);
    }

    /*---------- Getter/Setters ----------*/

    public CommandContext getCommandContext() {
        return commandContext;
    }

    public void setCommandContext(CommandContext commandContext) {
        this.commandContext = commandContext;
    }

    public LinkedList<Runnable> getOperations() {
        return operations;
    }

}
