package com.kaaterskil.workflow.engine.behavior.handler;

import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.engine.delegate.ActivityBehavior;
import com.kaaterskil.workflow.engine.parser.factory.ActivityBehaviorFactory;

public interface BehaviorHandler {
    public final String IMPLEMENTATION_TYPE_WEBSERVICE = "##WebService";

    Class<? extends FlowNode> getHandledType();

    ActivityBehavior getBehavior(ActivityBehaviorFactory factory, FlowNode element);
}
