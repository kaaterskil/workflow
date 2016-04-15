package com.kaaterskil.workflow.engine.deploy;

import com.kaaterskil.workflow.engine.persistence.entity.DeploymentEntity;

public interface Deployer {

    void deploy(DeploymentEntity deployment);
}
