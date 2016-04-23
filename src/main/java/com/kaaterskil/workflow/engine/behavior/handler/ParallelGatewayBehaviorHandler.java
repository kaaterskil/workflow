package com.kaaterskil.workflow.engine.behavior.handler;

import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.bpm.common.gateway.ParallelGateway;
import com.kaaterskil.workflow.engine.delegate.ActivityBehavior;
import com.kaaterskil.workflow.engine.parser.factory.ActivityBehaviorFactory;

public class ParallelGatewayBehaviorHandler extends AbstractBehaviorHandler<ParallelGateway> {

    @Override
    public Class<? extends FlowNode> getHandledType() {
        return ParallelGateway.class;
    }

    @Override
    protected ActivityBehavior instantiateBehavior(ActivityBehaviorFactory factory,
            ParallelGateway element) {
        return factory.createParallelGatewayActivityBehavior(element);
    }

}
