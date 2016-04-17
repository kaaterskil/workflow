package com.kaaterskil.workflow.engine.util;

import com.kaaterskil.workflow.bpm.common.process.Process;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.persistence.entity.ProcessDefinitionEntity;
import com.kaaterskil.workflow.engine.service.DeploymentService;

public class ProcessDefinitionUtil {

    public static ProcessDefinitionEntity getProcessDefinition(Long processDefinitionId) {
        return Context.getProcessEngineService().getDeploymentService()
                .findDeployedProcessDefinitionById(processDefinitionId);
    }

    public static Process getProcess(Long processDefinitionId) {
        final DeploymentService deploymentService = Context.getCommandContext()
                .getDeploymentService();
        final ProcessDefinitionEntity processDefinition = deploymentService
                .findDeployedProcessDefinitionById(processDefinitionId);
        return deploymentService.resolveProcessDefinition(processDefinition).getProcess();
    }
}
