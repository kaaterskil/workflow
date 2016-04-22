package com.kaaterskil.workflow.engine.parser.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.bpm.common.event.CancelEventDefinition;
import com.kaaterskil.workflow.bpm.common.event.EndEvent;
import com.kaaterskil.workflow.bpm.common.event.ErrorEventDefinition;
import com.kaaterskil.workflow.bpm.common.event.EventDefinition;
import com.kaaterskil.workflow.bpm.common.event.TerminateEventDefinition;
import com.kaaterskil.workflow.bpm.foundation.BaseElement;
import com.kaaterskil.workflow.engine.behavior.CancelEndEventActivityBehavior;
import com.kaaterskil.workflow.engine.behavior.ErrorEndEventActivityBehavior;
import com.kaaterskil.workflow.engine.behavior.NoneEndEventActivityBehavior;
import com.kaaterskil.workflow.engine.behavior.TerminateEndEventActivityBehavior;
import com.kaaterskil.workflow.engine.parser.BpmParser;
import com.kaaterskil.workflow.util.CollectionUtil;

public class EndEventParseHandler extends AbstractParseHandler<EndEvent> {
    private static final Logger log = LoggerFactory.getLogger(EndEventParseHandler.class);

    @Override
    protected Class<? extends BaseElement> getHandledType() {
        return EndEvent.class;
    }

    @Override
    protected void executeParse(BpmParser parser, EndEvent element) {
        log.debug("Parsing end event");

        if (CollectionUtil.isNotEmpty(element.getEventDefinitions())) {
            final EventDefinition eventDefinition = element.getEventDefinitions().get(0);

            if (eventDefinition instanceof ErrorEventDefinition) {
                element.setBehavior(ErrorEndEventActivityBehavior.class.getCanonicalName());
            } else if (eventDefinition instanceof TerminateEventDefinition) {
                element.setBehavior(TerminateEndEventActivityBehavior.class.getCanonicalName());
            } else if (eventDefinition instanceof CancelEventDefinition) {
                element.setBehavior(CancelEndEventActivityBehavior.class.getCanonicalName());
            } else {
                element.setBehavior(NoneEndEventActivityBehavior.class.getCanonicalName());
            }

        } else {
            element.setBehavior(NoneEndEventActivityBehavior.class.getCanonicalName());
        }
    }

}
