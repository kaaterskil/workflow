package com.kaaterskil.workflow.engine.behavior.handler;

import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.bpm.common.activity.SubProcess;
import com.kaaterskil.workflow.engine.delegate.ActivityBehavior;
import com.kaaterskil.workflow.engine.parser.factory.ActivityBehaviorFactory;

public class SubProcessBehaviorHandler extends AbstractBehaviorHandler<SubProcess> {

    @Override
    public Class<? extends FlowNode> getHandledType() {
        return SubProcess.class;
    }

    @Override
    protected ActivityBehavior instantiateBehavior(ActivityBehaviorFactory factory,
            SubProcess element) {
        return factory.createSubProcessActivityBehavior(element);
    }

}
