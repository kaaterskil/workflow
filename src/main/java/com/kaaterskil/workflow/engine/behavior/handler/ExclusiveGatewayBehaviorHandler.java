package com.kaaterskil.workflow.engine.behavior.handler;

import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.bpm.common.gateway.ExclusiveGateway;
import com.kaaterskil.workflow.engine.delegate.ActivityBehavior;
import com.kaaterskil.workflow.engine.parser.factory.ActivityBehaviorFactory;

public class ExclusiveGatewayBehaviorHandler extends AbstractBehaviorHandler<ExclusiveGateway> {

    @Override
    public Class<? extends FlowNode> getHandledType() {
        return ExclusiveGateway.class;
    }

    @Override
    protected ActivityBehavior instantiateBehavior(ActivityBehaviorFactory factory,
            ExclusiveGateway element) {
        return factory.createExclusiveGatewayActivityBehavior(element);
    }

}
