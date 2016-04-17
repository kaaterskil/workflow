package com.kaaterskil.workflow.engine.operation;

import java.util.List;

import com.kaaterskil.workflow.bpm.HasTokenListeners;
import com.kaaterskil.workflow.bpm.ImplementationType;
import com.kaaterskil.workflow.bpm.Listener;
import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.bpm.common.process.Process;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.TokenListener;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.parser.ListenerFactory;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.util.ProcessDefinitionUtil;

public abstract class AbstractOperation implements Runnable {

    protected CommandContext commandContext;
    protected Workflow workflow;
    protected Token token;

    public AbstractOperation(CommandContext commandContext, Token token) {
        this.commandContext = commandContext;
        this.workflow = commandContext.getWorkflow();
        this.token = token;
    }

    protected FlowElement findCurrentFlowElement(Token token) {
        final Long processDefinitionId = token.getProcessDefinitionId();
        final Process process = ProcessDefinitionUtil.getProcess(processDefinitionId);

        final String activityId = token.getCurrentActivityId();
        final FlowElement currentFlowElement = process.getFlowElement(activityId, true);
        token.setCurrentFlowElement(currentFlowElement);

        return currentFlowElement;
    }

    protected void executeTokenListeners(HasTokenListeners element, String eventType) {
        executeTokenListeners(element, null, eventType);
    }

    protected void executeTokenListeners(HasTokenListeners element,
            Token tokenToUseForListener, String eventType) {
        final List<Listener> listeners = element.getTokenListeners();
        final ListenerFactory listenerFactory = Context.getProcessEngineService().getListenerFactory();

        if (listeners != null) {
            for (final Listener listener : listeners) {
                if(eventType.equals(listener.getEventRefs())) {
                    TokenListener tokenListener = null;

                    if(listener.getImplementationType().equals(ImplementationType.CLASS)) {
                        tokenListener = listenerFactory.createClassDelegateTokenListener(listener);
                    } else if(listener.getImplementationType().equals(ImplementationType.INSTANCE)) {
                        tokenListener = (TokenListener) listener.getInstance();
                    }

                    Token tokenToUse = token;
                    if(tokenToUseForListener != null) {
                        tokenToUse = tokenToUseForListener;
                    }

                    if(tokenListener != null) {
                        tokenToUse.setEventName(eventType);
                        tokenListener.notify(tokenToUse);
                        tokenToUse.setEventName(null);
                    }
                }
            }
        }
    }

    /*---------- Getter/Setters ----------*/

    public CommandContext getCommandContext() {
        return commandContext;
    }

    public void setCommandContext(CommandContext commandContext) {
        this.commandContext = commandContext;
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
