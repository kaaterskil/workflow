package com.kaaterskil.workflow.engine.behavior;

import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.persistence.entity.Token;

public class NoneEndEventActivityBehavior extends FlowNodeActivityBehavior {

    @Override
    public void execute(DelegateToken token) {
        Context.getWorkflow().addOutgoingSequenceFlow((Token) token);
    }

}
