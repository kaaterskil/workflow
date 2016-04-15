package com.kaaterskil.workflow.engine.service;

import java.util.List;

import com.kaaterskil.workflow.engine.deploy.Deployer;
import com.kaaterskil.workflow.engine.deploy.DeploymentCache;
import com.kaaterskil.workflow.engine.deploy.ProcessDefinitionCacheEntry;
import com.kaaterskil.workflow.engine.persistence.entity.DeploymentEntity;
import com.kaaterskil.workflow.engine.persistence.entity.ProcessDefinitionEntity;

public interface DeploymentService {

    List<Deployer> getDeployers();

    void setDeployers(List<Deployer> deployers);

    DeploymentCache<ProcessDefinitionCacheEntry> getProcessDefinitionCache();

    void setProcessDefinitionCache(
            DeploymentCache<ProcessDefinitionCacheEntry> processDefinitionCache);

    void deploy(DeploymentEntity deployment);

    ProcessDefinitionEntity findDeployedProcessDefinitionById(Long processDefinitionId);

    ProcessDefinitionEntity findDeployedProcessDefinitionByKey(String processDefinitionKey);

    ProcessDefinitionCacheEntry resolveProcessDefinition(ProcessDefinitionEntity processDefinition);

    DeploymentEntity create();

    DeploymentEntity save(DeploymentEntity deployment);
}
