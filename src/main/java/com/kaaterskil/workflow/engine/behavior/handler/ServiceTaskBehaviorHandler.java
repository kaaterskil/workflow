package com.kaaterskil.workflow.engine.behavior.handler;

import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.bpm.common.activity.ServiceTask;
import com.kaaterskil.workflow.engine.delegate.ActivityBehavior;
import com.kaaterskil.workflow.engine.parser.factory.ActivityBehaviorFactory;
import com.kaaterskil.workflow.util.ValidationUtils;

public class ServiceTaskBehaviorHandler extends AbstractBehaviorHandler<ServiceTask> {

    @Override
    public Class<? extends FlowNode> getHandledType() {
        return ServiceTask.class;
    }

    @Override
    protected ActivityBehavior instantiateBehavior(ActivityBehaviorFactory factory,
            ServiceTask element) {
        if (!ValidationUtils.isEmptyOrWhitespace(element.getImplementation())) {
            final String implementation = element.getImplementation();
            if (!implementation.equalsIgnoreCase(IMPLEMENTATION_TYPE_WEBSERVICE)) {
                return factory.createClassDelegateServiceTask(element);
            }
            // TODO Implement WebService
        }
        return null;
    }

}
