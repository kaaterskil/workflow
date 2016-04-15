package com.kaaterskil.workflow.engine.command;

import java.util.Date;

import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventFactory;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventType;
import com.kaaterskil.workflow.engine.deploy.DeploymentFactory;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.DeploymentEntity;

public class DeployCommand implements Command<DeploymentEntity> {

    private final DeploymentFactory factory;

    public DeployCommand(DeploymentFactory factory) {
        this.factory = factory;
    }

    @Override
    public DeploymentEntity execute(CommandContext commandContext) {
        return executeDeploy(commandContext);
    }

    private DeploymentEntity executeDeploy(CommandContext commandContext) {
        DeploymentEntity deployment = factory.getDeployment();
        deployment.setDeployedAt(new Date());
        deployment.setNew(true);

        deployment = commandContext.getDeploymentService().save(deployment);

        commandContext.getProcessEngineService().getEventDispatcher()
                .dispatchEvent(WorkflowEventFactory
                        .createEntityEvent(WorkflowEventType.ENTITY_CREATED, deployment));

        commandContext.getDeploymentService().deploy(deployment);

        commandContext.getProcessEngineService().getEventDispatcher()
                .dispatchEvent(WorkflowEventFactory
                        .createEntityEvent(WorkflowEventType.ENTITY_INITIALIZED, deployment));

        return deployment;
    }

}
