package com.kaaterskil.workflow.engine.parser.handler;

import com.kaaterskil.workflow.bpm.common.activity.Transaction;
import com.kaaterskil.workflow.bpm.foundation.BaseElement;
import com.kaaterskil.workflow.engine.behavior.TransactionActivityBehavior;
import com.kaaterskil.workflow.engine.parser.BpmParser;

public class TransactionParseHandler extends AbstractActivityParseHandler<Transaction> {

    @Override
    protected Class<? extends BaseElement> getHandledType() {
        return Transaction.class;
    }

    @Override
    protected void executeParse(BpmParser parser, Transaction element) {
        element.setBehavior(TransactionActivityBehavior.class.getCanonicalName());

        parser.processFlowElements(element.getFlowElements());
    }

}
