package com.kaaterskil.workflow.engine.parser.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.bpm.common.event.StartEvent;
import com.kaaterskil.workflow.bpm.foundation.BaseElement;
import com.kaaterskil.workflow.engine.behavior.NoneStartEventActivityBehavior;
import com.kaaterskil.workflow.engine.parser.BpmParser;

public class StartEventParseHandler extends AbstractParseHandler<StartEvent> {
    private static final Logger log = LoggerFactory.getLogger(StartEventParseHandler.class);

    @Override
    protected Class<? extends BaseElement> getHandledType() {
        return StartEvent.class;
    }

    @Override
    protected void executeParse(BpmParser parser, StartEvent element) {
        log.debug("Parsing start event");

        if (element.getEventDefinitions() == null || element.getEventDefinitions().isEmpty()) {
            element.setBehavior(NoneStartEventActivityBehavior.class.getCanonicalName());
        }

        if (element.getSubProcess() == null && (element.getEventDefinitions().isEmpty())
                || parser.getProcess().getInitialFlowElement() == null) {
            parser.getProcess().setInitialFlowElement(element);
        }
    }

}
