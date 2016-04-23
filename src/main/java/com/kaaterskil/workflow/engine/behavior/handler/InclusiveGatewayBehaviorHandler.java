package com.kaaterskil.workflow.engine.behavior.handler;

import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.bpm.common.gateway.InclusiveGateway;
import com.kaaterskil.workflow.engine.delegate.ActivityBehavior;
import com.kaaterskil.workflow.engine.parser.factory.ActivityBehaviorFactory;

public class InclusiveGatewayBehaviorHandler extends AbstractBehaviorHandler<InclusiveGateway> {

    @Override
    public Class<? extends FlowNode> getHandledType() {
        return InclusiveGateway.class;
    }

    @Override
    protected ActivityBehavior instantiateBehavior(ActivityBehaviorFactory factory,
            InclusiveGateway element) {
        return factory.createInclusiveGatewayActivityBehavior(element);
    }

}
