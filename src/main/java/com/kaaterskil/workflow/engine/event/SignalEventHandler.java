package com.kaaterskil.workflow.engine.event;

import java.util.Map;

import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.EventSubscription;
import com.kaaterskil.workflow.engine.persistence.entity.EventSubscriptionType;
import com.kaaterskil.workflow.engine.persistence.entity.ProcessDefinitionEntity;
import com.kaaterskil.workflow.engine.service.DeploymentService;
import com.kaaterskil.workflow.engine.util.ProcessInstanceUtil;

public class SignalEventHandler extends AbstractEventHandler {

    @Override
    public EventSubscriptionType getEventHandlerType() {
        return EventSubscriptionType.SIGNAL;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handleEvent(EventSubscription eventSubscription, Object payload,
            CommandContext commandContext) {
        if (eventSubscription.getToken() != null) {
            super.handleEvent(eventSubscription, payload, commandContext);

        } else if (eventSubscription.getProcessDefinitionId() != null) {
            final DeploymentService deploymentService = Context.getProcessEngineService()
                    .getDeploymentService();

            final Long processDefinitionId = eventSubscription.getProcessDefinitionId();
            final ProcessDefinitionEntity processDefinition = deploymentService
                    .findDeployedProcessDefinitionById(processDefinitionId);

            Map<String, Object> variables = null;
            if (payload != null && payload instanceof Map) {
                variables = (Map<String, Object>) payload;
            }

            ProcessInstanceUtil.createAndStartProcessInstance(processDefinition, variables);
        } else {
            throw new WorkflowException(
                    "Invalid signal handling: No process definition or execution set");
        }
    }

}
