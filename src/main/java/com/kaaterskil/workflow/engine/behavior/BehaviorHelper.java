package com.kaaterskil.workflow.engine.behavior;

import java.util.HashMap;
import java.util.Map;

import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.engine.behavior.handler.BehaviorHandler;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.ActivityBehavior;
import com.kaaterskil.workflow.engine.parser.factory.ActivityBehaviorFactory;
import com.kaaterskil.workflow.engine.persistence.entity.Token;

public class BehaviorHelper {

    private Map<Class<? extends FlowNode>, BehaviorHandler> behaviorHandlers = new HashMap<>();

    public BehaviorHandler getHandlerForType(Class<? extends FlowNode> type) {
        return behaviorHandlers.get(type);
    }

    public void addHandler(BehaviorHandler handler) {
        final Class<? extends FlowNode> type = handler.getHandledType();
        if (!behaviorHandlers.containsKey(type)) {
            behaviorHandlers.put(type, handler);
        }
    }

    public ActivityBehavior getBehavior(FlowNode element) {
        final ActivityBehaviorFactory factory = Context.getProcessEngineService()
                .getActivityBehaviorFactory();

        final BehaviorHandler handler = behaviorHandlers.get(element.getClass());
        if (handler != null) {
            return handler.getBehavior(factory, element);
        }
        return null;
    }

    public void performOutgoingBehavior(Token token) {
        performOutgoingBehavior(token, true);
    }

    public void performOutgoingBehaviorIgnoreConditions(Token token) {
        performOutgoingBehavior(token, false);
    }

    private void performOutgoingBehavior(Token token, boolean checkConditions) {
        Context.getCommandContext().getWorkflow().addOutgoingSequenceFlow(token, checkConditions);
    }

    /*---------- Getter/Setters ----------*/

    public Map<Class<? extends FlowNode>, BehaviorHandler> getBehaviorHandlers() {
        return behaviorHandlers;
    }

    public void setBehaviorHandlers(
            Map<Class<? extends FlowNode>, BehaviorHandler> behaviorHandlers) {
        this.behaviorHandlers = behaviorHandlers;
    }
}
