package com.kaaterskil.workflow.engine.behavior;

import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.persistence.entity.Token;

public class BehaviorHelper {

    public void performOutgoingBehavior(Token token) {
        performOutgoingBehavior(token, true);
    }

    public void performOutgoingBehaviorIgnoreConditions(Token token) {
        performOutgoingBehavior(token, false);
    }

    private void performOutgoingBehavior(Token token, boolean checkConditions) {
        Context.getCommandContext().getWorkflow().addOutgoingSequenceFlow(token, checkConditions);
    }
}
