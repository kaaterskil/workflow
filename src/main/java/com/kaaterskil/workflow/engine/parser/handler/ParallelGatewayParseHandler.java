package com.kaaterskil.workflow.engine.parser.handler;

import com.kaaterskil.workflow.bpm.common.gateway.ParallelGateway;
import com.kaaterskil.workflow.bpm.foundation.BaseElement;
import com.kaaterskil.workflow.engine.behavior.ParallelGatewayActivityBehavior;
import com.kaaterskil.workflow.engine.parser.BpmParser;

public class ParallelGatewayParseHandler extends AbstractActivityParseHandler<ParallelGateway> {

    @Override
    protected Class<? extends BaseElement> getHandledType() {
        return ParallelGateway.class;
    }

    @Override
    protected void executeParse(BpmParser parser, ParallelGateway element) {
        element.setBehavior(ParallelGatewayActivityBehavior.class.getCanonicalName());

    }

}
