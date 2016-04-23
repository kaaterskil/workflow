package com.kaaterskil.workflow.engine.behavior;

import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.bpm.common.activity.Activity;
import com.kaaterskil.workflow.bpm.common.activity.SubProcess;
import com.kaaterskil.workflow.bpm.common.activity.Transaction;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.util.ScopeUtil;
import com.kaaterskil.workflow.util.CollectionUtil;

public class SequentialMultiInstanceBehavior extends MultiInstanceActivityBehavior {

    public SequentialMultiInstanceBehavior(Activity activity,
            BaseActivityBehavior innerActivityBehavior) {
        super(activity, innerActivityBehavior);
    }

    @Override
    protected int createInstances(DelegateToken multiInstanceToken) {
        final int numInstances = resolveNumberOfInstances(multiInstanceToken);
        if (numInstances == 0) {
            return numInstances;
        } else if (numInstances < 0) {
            throw new IllegalArgumentException(
                    "Invalid number of instances: Must be a non-negative integer, but was "
                            + numInstances);
        }

        final Token token = Context.getCommandContext().getTokenService()
                .createChildToken((Token) multiInstanceToken);
        token.setCurrentFlowElement(multiInstanceToken.getCurrentFlowElement());
        multiInstanceToken.setCurrentFlowElement(null);
        multiInstanceToken.setMultiInstanceRoot(true);

        setLoopVariable(multiInstanceToken, NUMBER_OFINSTANCES, numInstances);
        setLoopVariable(multiInstanceToken, NUMBER_OF_ACTIVE_INSTANCES, 1);
        setLoopVariable(multiInstanceToken, NUMBER_OF_COMPLETED_INSTANCES, 0);
        setLoopVariable(multiInstanceToken, getLoopCounterKey(), 0);
        setLoopVariable(token, getLoopCounterKey(), 0);

        if (numInstances > 0) {
            executeOriginalBehavior(token, 0);
        }

        return numInstances;
    }

    @Override
    public void leave(DelegateToken delegateToken) {
        final int numInstances = getLoopVariable(delegateToken, NUMBER_OFINSTANCES);
        final int loopCounter = getLoopVariable(delegateToken, getLoopCounterKey())
                + 1;
        final int numCompletedInstances = getLoopVariable(delegateToken,
                NUMBER_OF_COMPLETED_INSTANCES) + 1;

        final DelegateToken multiInstanceRootToken = getMultiInstanceRootToken(delegateToken);
        setLoopVariable(multiInstanceRootToken, NUMBER_OF_COMPLETED_INSTANCES,
                numCompletedInstances);
        setLoopVariable(multiInstanceRootToken, getLoopCounterKey(), loopCounter);
        setLoopVariable(delegateToken, getLoopCounterKey(), loopCounter);

        callActivityEndListeners(delegateToken);

        if (loopCounter >= numInstances || completionConditionSatisfied(delegateToken)) {
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
                            // TODO loop through boundary event refs to instantiate them and extract
                            // the event definition. If the event definition is an instance of
                            // CompensationEventDefinition, set the hasCompensation variable to true
                            // and break the loop.
                        }
                    }
                }
            }

            if (hasCompensation) {
                final Token token = (Token) delegateToken;
                ScopeUtil.createCopyOfSubProcessTokenForCompensation(token, token.getParent());
            }

            removeLoopVariable(multiInstanceRootToken, getLoopCounterKey());
            removeLoopVariable(delegateToken, getLoopCounterKey());
            multiInstanceRootToken.setMultiInstanceRoot(false);
            multiInstanceRootToken.setCurrentFlowElement(delegateToken.getCurrentFlowElement());

            Context.getCommandContext().getTokenService()
                    .deleteChildTokens((Token) multiInstanceRootToken, "MI_END");

            super.leave(multiInstanceRootToken);
        } else {
            executeOriginalBehavior(delegateToken, loopCounter);
        }
    }

}
