package com.kaaterskil.workflow.engine.parser.handler;

import com.kaaterskil.workflow.bpm.common.gateway.InclusiveGateway;
import com.kaaterskil.workflow.bpm.foundation.BaseElement;
import com.kaaterskil.workflow.engine.behavior.InclusiveGatewayActivityBehavior;
import com.kaaterskil.workflow.engine.parser.BpmParser;

public class InclusiveGatewayParseHandler extends AbstractActivityParseHandler<InclusiveGateway> {

    @Override
    protected Class<? extends BaseElement> getHandledType() {
        return InclusiveGateway.class;
    }

    @Override
    protected void executeParse(BpmParser parser, InclusiveGateway element) {
        element.setBehavior(InclusiveGatewayActivityBehavior.class.getCanonicalName());
    }

}
