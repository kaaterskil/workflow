package com.kaaterskil.workflow.engine.behavior.handler;

import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.bpm.common.gateway.EventBasedGateway;
import com.kaaterskil.workflow.engine.delegate.ActivityBehavior;
import com.kaaterskil.workflow.engine.parser.factory.ActivityBehaviorFactory;

public class EventBasedGatewayBehaviorHandler extends AbstractBehaviorHandler<EventBasedGateway> {

    @Override
    public Class<? extends FlowNode> getHandledType() {
        return EventBasedGateway.class;
    }

    @Override
    protected ActivityBehavior instantiateBehavior(ActivityBehaviorFactory factory,
            EventBasedGateway element) {
        return factory.createEventBasedGatewayActivityBehavior(element);
    }

}
