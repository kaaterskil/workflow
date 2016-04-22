package com.kaaterskil.workflow.engine.behavior;

import java.util.List;

import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.bpm.common.activity.SubProcess;
import com.kaaterskil.workflow.bpm.common.event.BoundaryEvent;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.service.TokenService;
import com.kaaterskil.workflow.engine.util.ScopeUtil;
import com.kaaterskil.workflow.util.CollectionUtil;

public class CancelEndEventActivityBehavior extends FlowNodeActivityBehavior {

    @Override
    public void execute(DelegateToken delegateToken) {
        final Token token = (Token) delegateToken;
        final CommandContext commandContext = Context.getCommandContext();
        final TokenService tokenService = commandContext.getTokenService();

        // Find cancel boundary event
        Token parentScope = null;
        Token target = tokenService.findById(token.getParentId());
        while (target != null && parentScope == null) {
            if (target.getCurrentFlowElement() instanceof SubProcess) {
                parentScope = target;
                final SubProcess subProcess = (SubProcess) target.getCurrentFlowElement();
                if (subProcess.getLoopCharacteristics() != null) {
                    final Token grandParentScope = parentScope.getParent();
                    final FlowElement flowElement = grandParentScope.getCurrentFlowElement();
                    if (flowElement != null && flowElement.getId().equals(subProcess.getId())) {
                        parentScope = grandParentScope;
                    }
                }

            } else {
                target = tokenService.findById(target.getParentId());
            }
        }
        if (parentScope == null) {
            throw new WorkflowException(
                    "No subprocess token found for cancel event " + token.getCurrentActivityId());
        }

        final SubProcess subProcess = (SubProcess) parentScope.getCurrentFlowElement();
        final BoundaryEvent cancelBoundaryEvent = null;
        if (CollectionUtil.isNotEmpty(subProcess.getBoundaryEventRefs())) {
            // TODO Loop through the boundaryEventRefs, instantiate each boundary event, fetch the
            // first event definition, and test if it is an instance of CancelEventDefinition. If
            // so, assign it to the local variable.
        }
        if (cancelBoundaryEvent == null) {
            throw new WorkflowException("Could not find cancel boundary event for Canel End Event "
                    + token.getCurrentActivityId());
        }

        @SuppressWarnings("unused")
        Token newParentScope = null;
        target = tokenService.findById(parentScope.getParentId());
        while (target != null && newParentScope == null) {
            if (target.isScope()) {
                newParentScope = target;
            } else {
                target = tokenService.findById(target.getParentId());
            }
        }
        if (newParentScope == null) {
            throw new WorkflowException("No parent scope token found for boundary event "
                    + cancelBoundaryEvent.getId());
        }

        ScopeUtil.createCopyOfSubProcessTokenForCompensation(parentScope, newParentScope);

        if (subProcess.getLoopCharacteristics() != null) {
            final List<? extends Token> multiInstanceTokens = parentScope.getChildTokens();
            for (final Token multiInstanceToken : multiInstanceTokens) {
                if (!multiInstanceToken.getId().equals(parentScope.getId())) {
                    ScopeUtil.createCopyOfSubProcessTokenForCompensation(multiInstanceToken,
                            newParentScope);
                    deleteChildTokens(multiInstanceToken, token, commandContext);
                }
            }
        }

        // Set new parent for boundary event token
        token.setParent(newParentScope);
        token.setCurrentFlowElement(cancelBoundaryEvent);

        // End all tokens in the transaction scope
        deleteChildTokens(parentScope, token, commandContext);

        commandContext.getWorkflow().addTriggerTokenOperation(token);
    }

    private void deleteChildTokens(Token parentToken, Token tokenToKeep,
            CommandContext commandContext) {
        final TokenService tokenService = commandContext.getTokenService();

        final List<Token> childTokens = tokenService
                .findChildTokensByParentTokenId(parentToken.getId());
        if (CollectionUtil.isNotEmpty(childTokens)) {
            for (final Token childToken : childTokens) {
                if (!childToken.getId().equals(tokenToKeep.getId())) {
                    deleteChildTokens(childToken, tokenToKeep, commandContext);
                }
            }
        }

        tokenService.deleteDataRelatedToToken(parentToken, null);
        tokenService.delete(parentToken);
    }
}
