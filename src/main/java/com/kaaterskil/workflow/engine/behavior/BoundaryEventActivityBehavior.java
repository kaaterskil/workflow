package com.kaaterskil.workflow.engine.behavior;

import java.util.List;

import com.kaaterskil.workflow.bpm.common.activity.CallActivity;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.service.TokenService;
import com.kaaterskil.workflow.util.CollectionUtil;

public class BoundaryEventActivityBehavior extends FlowNodeActivityBehavior {

    private boolean interrupting;

    public BoundaryEventActivityBehavior(boolean interrupting) {
        this.interrupting = interrupting;
    }

    public BoundaryEventActivityBehavior() {
    }

    /*---------- Methods ----------*/

    @Override
    public void trigger(DelegateToken delegateToken, String signalEvent, Object signalData) {
        final Token token = (Token) delegateToken;
        final CommandContext commandContext = Context.getCommandContext();

        if (interrupting) {
            executeInterruptingBehavior(token, commandContext);
        } else {
            executeNonInterruptingBehavior(token, commandContext);
        }
    }

    private void executeInterruptingBehavior(Token token, CommandContext commandContext) {
        final TokenService tokenService = commandContext.getTokenService();
        final Token attachedScope = tokenService.findById(token.getParentId());

        Token parentScope = null;
        Token target = tokenService.findById(attachedScope.getParentId());
        while (target != null && parentScope == null) {
            if (target.isScope()) {
                parentScope = target;
            } else {
                target = tokenService.findById(target.getParentId());
            }
        }
        if (parentScope == null) {
            throw new WorkflowException("No parent scope found for boundary event");
        }

        deleteChildTokens(attachedScope, token, commandContext);

        // Set new parent for boundary event execution
        token.setParent(parentScope);
        commandContext.getWorkflow().addOutgoingSequenceFlow(token, true);
    }

    private void executeNonInterruptingBehavior(Token token, CommandContext commandContext) {
        final TokenService tokenService = commandContext.getTokenService();
        final Token parentToken = tokenService.findById(token.getParentId());

        Token scopeToken = null;
        Token target = tokenService.findById(parentToken.getParentId());
        while(target != null && scopeToken == null) {
            if(target.isScope()) {
                scopeToken = target;
            } else {
                target = tokenService.findById(target.getParentId());
            }
        }
        if(scopeToken == null) {
            throw new WorkflowException("No parent scope token found for boundary event");
        }

        final Token nonInterruptingToken = tokenService.createChildToken(scopeToken);
        nonInterruptingToken.setCurrentFlowElement(token.getCurrentFlowElement());
        commandContext.getWorkflow().addOutgoingSequenceFlow(nonInterruptingToken, true);
    }

    private void deleteChildTokens(Token parentToken, Token tokenToKeep,
            CommandContext commandContext) {

        final TokenService tokenService = commandContext.getTokenService();
        final List<Token> childTokens = tokenService.findChildTokensByParentTokenId(parentToken.getId());
        if(CollectionUtil.isNotEmpty(childTokens)) {
            for(final Token childToken : childTokens) {
                if(!childToken.getId().equals(tokenToKeep.getId())) {
                    deleteChildTokens(childToken, tokenToKeep, commandContext);
                }
            }
        }

        if(parentToken.getCurrentFlowElement() instanceof CallActivity) {
            // TODO Implement superToken
        }

        tokenService.deleteDataRelatedToToken(parentToken, null);
        tokenService.delete(parentToken);
    }

    /*---------- Getter/Setters ----------*/

    public boolean isInterrupting() {
        return interrupting;
    }

    public void setInterrupting(boolean interrupting) {
        this.interrupting = interrupting;
    }
}
