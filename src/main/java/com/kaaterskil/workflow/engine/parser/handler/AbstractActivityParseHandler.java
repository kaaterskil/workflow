package com.kaaterskil.workflow.engine.parser.handler;

import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.bpm.common.activity.Activity;
import com.kaaterskil.workflow.bpm.common.activity.MultiInstanceLoopCharacteristics;
import com.kaaterskil.workflow.bpm.foundation.BaseElement;
import com.kaaterskil.workflow.engine.behavior.BaseActivityBehavior;
import com.kaaterskil.workflow.engine.behavior.BehaviorHelper;
import com.kaaterskil.workflow.engine.behavior.MultiInstanceActivityBehavior;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.parser.BpmParser;

public abstract class AbstractActivityParseHandler<T extends FlowNode>
        extends AbstractFlowNodeParseHandler<T> {

    @Override
    public void parse(BpmParser parser, BaseElement target) {
        super.parse(parser, target);

        if (target instanceof Activity) {
            final Activity activity = (Activity) target;
            if (activity.getLoopCharacteristics() != null) {
                createMultiInstanceLoopCharacteristics(parser, activity);
            }
        }
    }

    private void createMultiInstanceLoopCharacteristics(BpmParser parser, Activity activity) {
        final MultiInstanceLoopCharacteristics loopCharacteristics = (MultiInstanceLoopCharacteristics) activity
                .getLoopCharacteristics();

        final BehaviorHelper behaviorHelper = Context.getProcessEngineService()
                .getBehaviorHelper();
        final BaseActivityBehavior innerActivityBehavior = (BaseActivityBehavior) behaviorHelper
                .getBehavior(activity);

        MultiInstanceActivityBehavior behavior = null;
        if (loopCharacteristics.isSequential()) {
            behavior = parser.getActivityBehaviorFactory()
                    .createSequentialMultiInstanceBehavior(activity, innerActivityBehavior);
        } else {
            behavior = parser.getActivityBehaviorFactory()
                    .createParallelMultiInstanceBehavior(activity, innerActivityBehavior);
        }
        activity.setBehavior(behavior.getClass().getCanonicalName());

        if (loopCharacteristics.getLoopCardinality() != null) {
            behavior.setLoopCardinalityExpression(loopCharacteristics.getLoopCardinality());
        }

        if (loopCharacteristics.getCompletionCondition() != null) {
            behavior.setCompletionConditionExpression(loopCharacteristics.getCompletionCondition());
        }

        // The name of a process variable which is a collection. For each item in the collection an
        // instance will be created.
        if (loopCharacteristics.getLoopDataInputRef() != null) {
            behavior.setCollectionVariable(loopCharacteristics.getLoopDataInputRef());
        }

        // The specific item of the collection.
        if (loopCharacteristics.getInputDataItem() != null) {
            behavior.setCollectionElementVariable(
                    loopCharacteristics.getInputDataItem().getItemSubjectRef());
        }
    }
}
