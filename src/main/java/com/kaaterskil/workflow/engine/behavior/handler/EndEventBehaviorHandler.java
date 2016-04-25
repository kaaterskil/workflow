package com.kaaterskil.workflow.engine.behavior.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.bpm.common.event.CancelEventDefinition;
import com.kaaterskil.workflow.bpm.common.event.EndEvent;
import com.kaaterskil.workflow.bpm.common.event.ErrorEventDefinition;
import com.kaaterskil.workflow.bpm.common.event.EventDefinition;
import com.kaaterskil.workflow.bpm.common.event.TerminateEventDefinition;
import com.kaaterskil.workflow.engine.delegate.ActivityBehavior;
import com.kaaterskil.workflow.engine.parser.factory.ActivityBehaviorFactory;
import com.kaaterskil.workflow.util.CollectionUtil;

public class EndEventBehaviorHandler extends AbstractBehaviorHandler<EndEvent> {
    private static final Logger log = LoggerFactory.getLogger(EndEventBehaviorHandler.class);

    @Override
    public Class<? extends FlowNode> getHandledType() {
        return EndEvent.class;
    }

    @Override
    protected ActivityBehavior instantiateBehavior(ActivityBehaviorFactory factory,
            EndEvent element) {
        if (CollectionUtil.isNotEmpty(element.getEventDefinitions())) {
            final EventDefinition eventDefinition = element.getEventDefinitions().get(0);

            if (eventDefinition instanceof ErrorEventDefinition) {
                final ErrorEventDefinition errorDefinition = (ErrorEventDefinition) eventDefinition;
                if (!factory.getBpmModel().containsErrorRef(errorDefinition.getErrorRef())) {
                    log.warn("An error code is required for an error event");
                }
                return factory.createErrorEndEventActivityBehavior(element, errorDefinition);

            } else if (eventDefinition instanceof TerminateEventDefinition) {
                return factory.createTerminateEndEventActivityBehavior(element);

            } else if (eventDefinition instanceof CancelEventDefinition) {
                return factory.createCancelEndEventActivityBehavior(element);

            } else {
                return factory.createNoneEndEventActivityBehavior(element);
            }

        }
        return factory.createNoneEndEventActivityBehavior(element);
    }

}
