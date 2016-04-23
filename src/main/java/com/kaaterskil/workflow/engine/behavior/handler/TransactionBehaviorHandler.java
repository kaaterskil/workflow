package com.kaaterskil.workflow.engine.behavior.handler;

import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.bpm.common.activity.Transaction;
import com.kaaterskil.workflow.engine.delegate.ActivityBehavior;
import com.kaaterskil.workflow.engine.parser.factory.ActivityBehaviorFactory;

public class TransactionBehaviorHandler extends AbstractBehaviorHandler<Transaction> {

    @Override
    public Class<? extends FlowNode> getHandledType() {
        return Transaction.class;
    }

    @Override
    protected ActivityBehavior instantiateBehavior(ActivityBehaviorFactory factory,
            Transaction element) {
        return factory.createTransactionActivityBehavior(element);
    }

}
