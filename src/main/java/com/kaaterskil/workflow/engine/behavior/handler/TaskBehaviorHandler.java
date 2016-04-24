package com.kaaterskil.workflow.engine.behavior.handler;

import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.bpm.common.activity.Task;
import com.kaaterskil.workflow.engine.delegate.ActivityBehavior;
import com.kaaterskil.workflow.engine.parser.factory.ActivityBehaviorFactory;

public class TaskBehaviorHandler extends AbstractBehaviorHandler<Task> {

    @Override
    public Class<? extends FlowNode> getHandledType() {
        return Task.class;
    }

    @Override
    protected ActivityBehavior instantiateBehavior(ActivityBehaviorFactory factory, Task element) {
        return factory.createTaskActivityBehavior(element);
    }

}
