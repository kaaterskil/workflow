package com.kaaterskil.workflow.engine.behavior.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.bpm.common.event.EventDefinition;
import com.kaaterskil.workflow.bpm.common.event.IntermediateCatchEvent;
import com.kaaterskil.workflow.bpm.common.event.MessageEventDefinition;
import com.kaaterskil.workflow.bpm.common.event.SignalEventDefinition;
import com.kaaterskil.workflow.bpm.common.event.TimerEventDefinition;
import com.kaaterskil.workflow.engine.behavior.BehaviorHelper;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.ActivityBehavior;
import com.kaaterskil.workflow.engine.parser.factory.ActivityBehaviorFactory;

public class IntermediateCatchEventBehaviorHandler
        extends AbstractBehaviorHandler<IntermediateCatchEvent> {
    private static final Logger log = LoggerFactory
            .getLogger(IntermediateCatchEventBehaviorHandler.class);

    @Override
    public Class<? extends FlowNode> getHandledType() {
        return IntermediateCatchEvent.class;
    }

    @Override
    protected ActivityBehavior instantiateBehavior(ActivityBehaviorFactory factory,
            IntermediateCatchEvent event) {

        EventDefinition eventDefinition = null;
        if (!event.getEventDefinitions().isEmpty()) {
            eventDefinition = event.getEventDefinitions().get(0);
        }

        if (eventDefinition == null) {
            return factory.createIntermediateCatchEventActivityBehavior(event);

        } else if (eventDefinition instanceof TimerEventDefinition
                || eventDefinition instanceof SignalEventDefinition
                || eventDefinition instanceof MessageEventDefinition) {
            final BehaviorHelper helper = Context.getProcessEngineService().getBehaviorHelper(null);
            return helper.getBehavior(event);
        }

        log.warn("Unsupported intermediate catch event type for event " + event.getId());
        return null;
    }

}
