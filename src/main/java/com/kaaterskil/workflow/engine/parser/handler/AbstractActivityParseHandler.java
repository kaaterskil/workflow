package com.kaaterskil.workflow.engine.parser.handler;

import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.bpm.common.activity.Activity;
import com.kaaterskil.workflow.bpm.common.activity.MultiInstanceLoopCharacteristics;
import com.kaaterskil.workflow.bpm.foundation.BaseElement;
import com.kaaterskil.workflow.engine.behavior.ParallelMultiInstanceBehavior;
import com.kaaterskil.workflow.engine.behavior.SequentialMultiInstanceBehavior;
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
        if (loopCharacteristics.isSequential()) {
            activity.setBehavior(SequentialMultiInstanceBehavior.class.getCanonicalName());
        } else {
            activity.setBehavior(ParallelMultiInstanceBehavior.class.getCanonicalName());
        }
    }
}
