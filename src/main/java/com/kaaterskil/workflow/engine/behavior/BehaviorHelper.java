package com.kaaterskil.workflow.engine.behavior;

import java.util.HashMap;
import java.util.Map;

import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.bpm.common.activity.Activity;
import com.kaaterskil.workflow.bpm.common.activity.MultiInstanceLoopCharacteristics;
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

    public ActivityBehavior getBehavior(FlowNode flowNode) {
        final ActivityBehaviorFactory factory = Context.getProcessEngineService()
                .getActivityBehaviorFactory();

        if (flowNode instanceof Activity) {
            final Activity activity = (Activity) flowNode;
            if (activity.getLoopCharacteristics() != null) {
                return createMultiInstanceActivityBehavior(factory, activity);
            }
        }

        final BehaviorHandler handler = behaviorHandlers.get(flowNode.getClass());
        if (handler != null) {
            return handler.getBehavior(factory, flowNode);
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

    private MultiInstanceActivityBehavior createMultiInstanceActivityBehavior(
            ActivityBehaviorFactory factory, Activity activity) {
        final MultiInstanceLoopCharacteristics loopCharacteristics = (MultiInstanceLoopCharacteristics) activity
                .getLoopCharacteristics();

        final BaseActivityBehavior innerActivityBehavior = (BaseActivityBehavior) getBehavior(
                activity);
        MultiInstanceActivityBehavior behavior = null;
        if (loopCharacteristics.isSequential()) {
            behavior = factory.createSequentialMultiInstanceBehavior(activity,
                    innerActivityBehavior);
        } else {
            behavior = factory.createParallelMultiInstanceBehavior(activity, innerActivityBehavior);
        }

        if (loopCharacteristics.getLoopCardinality() != null) {
            behavior.setLoopCardinalityExpression(loopCharacteristics.getLoopCardinality());
        }

        if (loopCharacteristics.getCompletionCondition() != null) {
            behavior.setCompletionConditionExpression(loopCharacteristics.getCompletionCondition());
        }

        // The name of a process variable which is a collection. For each item in the collection an
        // instance will be created.
        if (loopCharacteristics.getLoopDataInputRef() != null) {
            behavior.setCollectionVariable(loopCharacteristics.getLoopDataInputRef());
        }

        // The specific item of the collection.
        if (loopCharacteristics.getInputDataItem() != null) {
            behavior.setCollectionElementVariable(
                    loopCharacteristics.getInputDataItem().getItemSubjectRef());
        }

        return behavior;
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
