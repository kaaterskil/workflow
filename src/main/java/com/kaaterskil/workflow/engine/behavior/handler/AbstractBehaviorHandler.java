package com.kaaterskil.workflow.engine.behavior.handler;

import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.engine.delegate.ActivityBehavior;
import com.kaaterskil.workflow.engine.parser.factory.ActivityBehaviorFactory;

public abstract class AbstractBehaviorHandler<T extends FlowNode> implements BehaviorHandler {

    @SuppressWarnings("unchecked")
    @Override
    public ActivityBehavior getBehavior(ActivityBehaviorFactory factory, FlowNode target) {
        final T element = (T) target;
        return instantiateBehavior(factory, element);
    }

    protected abstract ActivityBehavior instantiateBehavior(ActivityBehaviorFactory factory,
            T element);

}
