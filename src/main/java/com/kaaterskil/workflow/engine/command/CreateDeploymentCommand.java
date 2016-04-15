package com.kaaterskil.workflow.engine.command;

import com.kaaterskil.workflow.engine.RepositoryService;
import com.kaaterskil.workflow.engine.deploy.DeploymentFactory;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;

public class CreateDeploymentCommand implements Command<DeploymentFactory> {

    @Override
    public DeploymentFactory execute(CommandContext commandContext) {
        final RepositoryService repositoryService = commandContext.getProcessEngineService()
                .getRepositoryService();
        return new DeploymentFactory(repositoryService);
    }

}
