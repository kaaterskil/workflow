package com.kaaterskil.workflow.engine.deploy;

import com.kaaterskil.workflow.engine.RepositoryService;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.persistence.entity.DeploymentEntity;

public class DeploymentFactory {

    private final RepositoryService repositoryService;
    private final DeploymentEntity deployment;

    public DeploymentFactory(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
        this.deployment = Context.getCommandContext().getDeploymentService().create();
    }

    public DeploymentEntity getDeployment() {
        return deployment;
    }

    public DeploymentFactory setName(String name) {
        deployment.setName(name);
        return this;
    }

    public String getName() {
        return deployment.getName();
    }

    public DeploymentEntity deploy(){
        return repositoryService.deploy(this);
    }
}
