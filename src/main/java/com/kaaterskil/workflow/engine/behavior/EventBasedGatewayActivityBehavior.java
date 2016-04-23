package com.kaaterskil.workflow.engine.behavior;

import com.kaaterskil.workflow.engine.delegate.DelegateToken;

public class EventBasedGatewayActivityBehavior extends FlowNodeActivityBehavior {

    @Override
    public void execute(DelegateToken token) {
        leave(token);
    }

}
