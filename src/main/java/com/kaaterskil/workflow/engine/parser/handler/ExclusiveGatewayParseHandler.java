package com.kaaterskil.workflow.engine.parser.handler;

import com.kaaterskil.workflow.bpm.common.gateway.ExclusiveGateway;
import com.kaaterskil.workflow.bpm.foundation.BaseElement;
import com.kaaterskil.workflow.engine.behavior.ExclusiveGatewayActivityBehavior;
import com.kaaterskil.workflow.engine.parser.BpmParser;

public class ExclusiveGatewayParseHandler extends AbstractActivityParseHandler<ExclusiveGateway> {

    @Override
    protected Class<? extends BaseElement> getHandledType() {
        return ExclusiveGateway.class;
    }

    @Override
    protected void executeParse(BpmParser parser, ExclusiveGateway element) {
        element.setBehavior(ExclusiveGatewayActivityBehavior.class.getCanonicalName());
    }

}
