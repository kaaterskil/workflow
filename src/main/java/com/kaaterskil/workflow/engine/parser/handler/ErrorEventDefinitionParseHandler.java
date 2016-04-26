package com.kaaterskil.workflow.engine.parser.handler;

import com.kaaterskil.workflow.bpm.common.event.BoundaryEvent;
import com.kaaterskil.workflow.bpm.common.event.ErrorEventDefinition;
import com.kaaterskil.workflow.bpm.foundation.BaseElement;
import com.kaaterskil.workflow.engine.behavior.BoundaryEventActivityBehavior;
import com.kaaterskil.workflow.engine.parser.BpmParser;

public class ErrorEventDefinitionParseHandler extends AbstractParseHandler<ErrorEventDefinition> {

    @Override
    protected Class<? extends BaseElement> getHandledType() {
        return ErrorEventDefinition.class;
    }

    @Override
    protected void executeParse(BpmParser parser, ErrorEventDefinition element) {
        if(parser.getCurrentFlowElement() instanceof BoundaryEvent) {
            final BoundaryEvent boundaryEvent = (BoundaryEvent) parser.getCurrentFlowElement();
            boundaryEvent.setBehavior(BoundaryEventActivityBehavior.class.getCanonicalName());
        }
    }

}
