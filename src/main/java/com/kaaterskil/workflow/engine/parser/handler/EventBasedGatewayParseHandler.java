package com.kaaterskil.workflow.engine.parser.handler;

import com.kaaterskil.workflow.bpm.common.gateway.EventBasedGateway;
import com.kaaterskil.workflow.bpm.foundation.BaseElement;
import com.kaaterskil.workflow.engine.behavior.EventBasedGatewayActivityBehavior;
import com.kaaterskil.workflow.engine.parser.BpmParser;

public class EventBasedGatewayParseHandler extends AbstractActivityParseHandler<EventBasedGateway> {

    @Override
    protected Class<? extends BaseElement> getHandledType() {
        return EventBasedGateway.class;
    }

    @Override
    protected void executeParse(BpmParser parser, EventBasedGateway element) {
        element.setBehavior(EventBasedGatewayActivityBehavior.class.getCanonicalName());
    }

}
