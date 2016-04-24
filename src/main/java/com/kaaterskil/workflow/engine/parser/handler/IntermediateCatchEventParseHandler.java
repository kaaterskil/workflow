package com.kaaterskil.workflow.engine.parser.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.bpm.common.event.EventDefinition;
import com.kaaterskil.workflow.bpm.common.event.IntermediateCatchEvent;
import com.kaaterskil.workflow.bpm.common.event.MessageEventDefinition;
import com.kaaterskil.workflow.bpm.common.event.SignalEventDefinition;
import com.kaaterskil.workflow.bpm.common.event.TimerEventDefinition;
import com.kaaterskil.workflow.bpm.foundation.BaseElement;
import com.kaaterskil.workflow.engine.behavior.IntermediateCatchEventActivityBehavior;
import com.kaaterskil.workflow.engine.parser.BpmParser;

public class IntermediateCatchEventParseHandler
        extends AbstractFlowNodeParseHandler<IntermediateCatchEvent> {
    private static final Logger log = LoggerFactory
            .getLogger(IntermediateCatchEventParseHandler.class);

    @Override
    protected Class<? extends BaseElement> getHandledType() {
        return IntermediateCatchEvent.class;
    }

    @Override
    protected void executeParse(BpmParser parser, IntermediateCatchEvent element) {
        EventDefinition eventDefinition = null;
        if (!element.getEventDefinitions().isEmpty()) {
            eventDefinition = element.getEventDefinitions().get(0);
        }

        if (eventDefinition == null) {
            element.setBehavior(IntermediateCatchEventActivityBehavior.class.getCanonicalName());

        } else if (eventDefinition instanceof TimerEventDefinition
                || eventDefinition instanceof SignalEventDefinition
                || eventDefinition instanceof MessageEventDefinition) {
            parser.getParseHelper().parseElement(parser, eventDefinition);

        } else {
            log.warn("Unsupported intermediate catch event type for event " + element.getId());
        }
    }

}
