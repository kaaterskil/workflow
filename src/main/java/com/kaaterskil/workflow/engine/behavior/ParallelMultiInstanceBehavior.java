package com.kaaterskil.workflow.engine.behavior;

import java.util.ArrayList;
import java.util.List;

import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.bpm.common.activity.Activity;
import com.kaaterskil.workflow.bpm.common.activity.SubProcess;
import com.kaaterskil.workflow.bpm.common.activity.Transaction;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.service.TokenService;
import com.kaaterskil.workflow.engine.util.ScopeUtil;
import com.kaaterskil.workflow.util.CollectionUtil;

public class ParallelMultiInstanceBehavior extends MultiInstanceActivityBehavior {

    public ParallelMultiInstanceBehavior(Activity activity,
            BaseActivityBehavior innerActivityBehavior) {
        super(activity, innerActivityBehavior);
    }

    @Override
    protected int createInstances(DelegateToken delegateToken) {
        final int numInstances = resolveNumberOfInstances(delegateToken);
        if (numInstances < 0) {
            throw new IllegalArgumentException(
                    "Invalid number of instances: Mus be non-negative but was " + numInstances);
        }

        delegateToken.setMultiInstanceRoot(true);

        setLoopVariable(delegateToken, NUMBER_OFINSTANCES, numInstances);
        setLoopVariable(delegateToken, NUMBER_OF_ACTIVE_INSTANCES, numInstances);
        setLoopVariable(delegateToken, NUMBER_OF_COMPLETED_INSTANCES, 0);

        final List<DelegateToken> concurrentTokens = new ArrayList<>();
        for (int idx = 0; idx < numInstances; idx++) {
            final DelegateToken concurrentToken = Context.getCommandContext().getTokenService()
                    .createChildToken((Token) delegateToken);
            concurrentToken.setCurrentFlowElement(activity);
            concurrentToken.setActive(true);
            concurrentToken.setScope(false);

            concurrentTokens.add(concurrentToken);
        }

        for (int idx = 0; idx < numInstances; idx++) {
            final DelegateToken concurrentToken = concurrentTokens.get(idx);
            if (concurrentToken.isActive() && !concurrentToken.isEnded()
                    && concurrentToken.getParent().isActive()
                    && !concurrentToken.getParent().isEnded()) {
                setLoopVariable(concurrentToken, getLoopCounterKey(), idx);
                executeOriginalBehavior(concurrentToken, idx);
            }
        }

        if (!concurrentTokens.isEmpty()) {
            final Token token = (Token) delegateToken;
            token.setActive(false);
        }

        return numInstances;
    }

    @Override
    public void leave(DelegateToken delegateToken) {
        boolean hasNoInstances = false;
        if (resolveNumberOfInstances(delegateToken) == 0) {
            hasNoInstances = true;
            removeLoopVariable(delegateToken, getLoopCounterKey());
            super.leave(delegateToken);
            delegateToken.setMultiInstanceRoot(false);
        }

        @SuppressWarnings("unused")
        final int loopCounter = getLoopVariable(delegateToken, getLoopCounterKey());
        final int numInstances = getLoopVariable(delegateToken, NUMBER_OFINSTANCES);
        final int numActiveInstances = getLoopVariable(delegateToken, NUMBER_OF_ACTIVE_INSTANCES)
                - 1;
        final int numCompletedInstances = getLoopVariable(delegateToken,
                NUMBER_OF_COMPLETED_INSTANCES) + 1;

        callActivityEndListeners(delegateToken);

        if (hasNoInstances) {
            return;
        }

        if (delegateToken.getParent() != null) {
            setLoopVariable(delegateToken.getParent(), NUMBER_OF_ACTIVE_INSTANCES,
                    numActiveInstances);
            setLoopVariable(delegateToken.getParent(), NUMBER_OF_COMPLETED_INSTANCES,
                    numCompletedInstances);
        }

        final Token token = (Token) delegateToken;
        if (token.getParent() != null) {
            token.setActive(false);
            lockFirstParentScope(token);

            if (numCompletedInstances >= numInstances
                    || completionConditionSatisfied(delegateToken.getParent())) {
                Token target = null;
                if (numInstances > 0) {
                    target = token.getParent();
                } else {
                    target = token;
                }

                boolean hasCompensation = false;
                final Activity activity = (Activity) delegateToken.getCurrentFlowElement();
                if (activity instanceof Transaction) {
                    hasCompensation = true;
                } else if (activity instanceof SubProcess) {
                    final SubProcess subProcess = (SubProcess) activity;
                    for (final FlowElement flowElement : subProcess.getFlowElements()) {
                        if (flowElement instanceof Activity) {
                            final Activity subActivity = (Activity) flowElement;
                            if (CollectionUtil.isNotEmpty(subActivity.getBoundaryEventRefs())) {
                                // TODO loop through boundary event refs to instantiate them and
                                // extract the event definition. If the event definition is an
                                // instance of CompensationEventDefinition, set the hasCompensation
                                // variable to true and break the loop.
                            }
                        }
                    }
                }
                if (hasCompensation) {
                    ScopeUtil.createCopyOfSubProcessTokenForCompensation(target,
                            target.getParent());
                }

                // TODO Handle CallActivity

                deleteChildTokens(target, false, Context.getCommandContext());
                Context.getWorkflow().addOutgoingSequenceFlow(target, true);
            }
        } else {
            removeLoopVariable(delegateToken, getLoopCounterKey());
            delegateToken.setMultiInstanceRoot(false);
            super.leave(delegateToken);
        }
    }

    protected void lockFirstParentScope(DelegateToken token) {
        final TokenService tokenService = Context.getCommandContext().getTokenService();

        boolean found = false;
        Token parentScope = null;
        Token currentToken = (Token) token;
        while (!found && currentToken != null && currentToken.getParentId() != null) {
            parentScope = tokenService.findById(currentToken.getParentId());
            if (parentScope != null && parentScope.isScope()) {
                found = true;
            }
            currentToken = parentScope;
        }

        parentScope.forceUpdate();
    }

    protected void deleteChildTokens(Token parentToken, boolean deleteToken,
            CommandContext commandContext) {
        final TokenService tokenService = commandContext.getTokenService();

        final List<Token> childTokens = tokenService
                .findChildTokensByParentTokenId(parentToken.getId());
        if (CollectionUtil.isNotEmpty(childTokens)) {
            for (final Token childToken : childTokens) {
                deleteChildTokens(childToken, true, commandContext);
            }
        }

        if (deleteToken) {
            tokenService.deleteDataRelatedToToken(parentToken, null);
            commandContext.getTokenService().delete(parentToken);
        }
    }

}
