package com.kaaterskil.workflow.engine.behavior.handler;

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
                final ErrorEventDefinition errorEventDefinition = (ErrorEventDefinition) eventDefinition;
                // TODO Implement an errorEventDefinition library somewhere so we can fetch the
                // error code from it.

                return factory.createErrorEndEventActivityBehavior(element, errorEventDefinition);

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
