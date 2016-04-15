package com.kaaterskil.workflow.engine.command;

import java.util.Map;

import com.kaaterskil.workflow.engine.exception.EntityNotFoundException;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.ProcessDefinitionEntity;
import com.kaaterskil.workflow.engine.runtime.ProcessInstance;
import com.kaaterskil.workflow.engine.service.DeploymentService;
import com.kaaterskil.workflow.engine.util.ProcessInstanceUtil;

public class StartProcessInstanceCommand implements Command<ProcessInstance> {

    private final String processDefinitionKey;
    private final Long processDefinitionId;
    private final Map<String, Object> variables;

    public StartProcessInstanceCommand(String processDefinitionKey, Long processDefinitionId,
            Map<String, Object> variables) {
        this.processDefinitionKey = processDefinitionKey;
        this.processDefinitionId = processDefinitionId;
        this.variables = variables;
    }

    @Override
    public ProcessInstance execute(CommandContext commandContext) {
        final DeploymentService deploymentService = commandContext.getProcessEngineService()
                .getDeploymentService();

        ProcessDefinitionEntity processDefinition = null;
        if (processDefinitionId != null) {
            processDefinition = deploymentService
                    .findDeployedProcessDefinitionById(processDefinitionId);
            if (processDefinition == null) {
                throw new EntityNotFoundException(
                        "No process definition found for id " + processDefinitionId);
            }

        } else if (processDefinitionKey != null) {
            processDefinition = deploymentService
                    .findDeployedProcessDefinitionByKey(processDefinitionKey);
            if (processDefinition == null) {
                throw new EntityNotFoundException(
                        "No process definition found for key " + processDefinitionKey);
            }

        } else {
            throw new IllegalArgumentException(
                    "process definition key and process definition id are both null");
        }

        return ProcessInstanceUtil.createAndStartProcessInstance(processDefinition, variables);
    }
}
