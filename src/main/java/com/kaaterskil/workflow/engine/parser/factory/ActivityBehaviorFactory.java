package com.kaaterskil.workflow.engine.parser.factory;

import com.kaaterskil.workflow.bpm.common.event.StartEvent;
import com.kaaterskil.workflow.engine.behavior.NoneStartEventActivityBehavior;

public class ActivityBehaviorFactory extends AbstractBehaviorFactory {

    public NoneStartEventActivityBehavior createNoneStartEventActivityBehavior(
            StartEvent startEvent) {
        return new NoneStartEventActivityBehavior();
    }
}
