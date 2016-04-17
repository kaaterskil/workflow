package com.kaaterskil.workflow.engine.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kaaterskil.workflow.engine.deploy.Deployer;
import com.kaaterskil.workflow.engine.deploy.DeploymentCache;
import com.kaaterskil.workflow.engine.deploy.ProcessDefinitionCacheEntry;
import com.kaaterskil.workflow.engine.exception.EntityNotFoundException;
import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.engine.persistence.entity.DeploymentEntity;
import com.kaaterskil.workflow.engine.persistence.entity.ProcessDefinitionEntity;
import com.kaaterskil.workflow.engine.persistence.repository.DeploymentRepository;
import com.kaaterskil.workflow.engine.persistence.repository.ProcessDefinitionRepository;
import com.kaaterskil.workflow.engine.service.DeploymentService;
import com.kaaterskil.workflow.util.ValidationUtils;

@Component
public class DeploymentServiceImpl implements DeploymentService {

    @Autowired
    private DeploymentRepository repository;
    @Autowired
    private ProcessDefinitionRepository processDefinitionRepository;

    private DeploymentCache<ProcessDefinitionCacheEntry> processDefinitionCache;
    private List<Deployer> deployers;

    /*---------- Methods ----------*/

    @Override
    public void deploy(DeploymentEntity deployment) {
        for (final Deployer deployer : deployers) {
            deployer.deploy(deployment);
        }
    }

    @Override
    public ProcessDefinitionEntity findDeployedProcessDefinitionById(Long processDefinitionId) {
        if (processDefinitionId == null) {
            throw new IllegalArgumentException("process definition id cannot be null");
        }

        ProcessDefinitionEntity processDefinition = null;
        final ProcessDefinitionCacheEntry cacheEntry = processDefinitionCache
                .get(processDefinitionId);
        if (cacheEntry != null) {
            processDefinition = cacheEntry.getProcessDefinitionEntity();
        }

        if (processDefinition == null) {
            processDefinition = processDefinitionRepository.findOne(processDefinitionId);
            if (processDefinition == null) {
                throw new EntityNotFoundException(
                        "No deployed process definition found with id " + processDefinitionId);
            }
            processDefinition = resolveProcessDefinition(processDefinition)
                    .getProcessDefinitionEntity();
        }

        return processDefinition;
    }

    @Override
    public ProcessDefinitionEntity findDeployedProcessDefinitionByKey(String processDefinitionKey) {
        if (ValidationUtils.isEmptyOrWhitespace(processDefinitionKey)) {
            throw new IllegalArgumentException("process definition key cannot be null");
        }

        ProcessDefinitionEntity processDefinition = processDefinitionRepository
                .findByKey(processDefinitionKey);
        if (processDefinition == null) {
            throw new EntityNotFoundException(
                    "No process deployed with key " + processDefinitionKey);
        }

        processDefinition = resolveProcessDefinition(processDefinition)
                .getProcessDefinitionEntity();
        return processDefinition;
    }

    @Override
    public ProcessDefinitionCacheEntry resolveProcessDefinition(
            ProcessDefinitionEntity processDefinition) {
        final Long processDefinitionId = processDefinition.getId();
        final Long deploymentId = processDefinition.getDeploymentId();

        ProcessDefinitionCacheEntry cacheEntry = processDefinitionCache.get(processDefinitionId);
        if (cacheEntry == null) {
            final DeploymentEntity deployment = repository.findOne(deploymentId);
            deployment.setNew(false);
            deploy(deployment);
            cacheEntry = processDefinitionCache.get(processDefinitionId);

            if (cacheEntry == null) {
                throw new WorkflowException("Deployment didn't save the process definition "
                        + processDefinitionId + " to the cache");
            }
        }
        return cacheEntry;
    }

    @Override
    public DeploymentEntity create() {
        return new DeploymentEntity();
    }

    @Override
    public DeploymentEntity save(DeploymentEntity deployment) {
        return repository.save(deployment);
    }

    /*---------- Getter/Setters ----------*/

    @Override
    public DeploymentCache<ProcessDefinitionCacheEntry> getProcessDefinitionCache() {
        return processDefinitionCache;
    }

    @Override
    public void setProcessDefinitionCache(
            DeploymentCache<ProcessDefinitionCacheEntry> processDefinitionCache) {
        this.processDefinitionCache = processDefinitionCache;
    }

    @Override
    public List<Deployer> getDeployers() {
        return deployers;
    }

    @Override
    public void setDeployers(List<Deployer> deployers) {
        this.deployers = deployers;
    }
}
