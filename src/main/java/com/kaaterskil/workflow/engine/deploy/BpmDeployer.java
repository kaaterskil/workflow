package com.kaaterskil.workflow.engine.deploy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventFactory;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventType;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.parser.BpmParser;
import com.kaaterskil.workflow.engine.persistence.entity.DeploymentEntity;
import com.kaaterskil.workflow.engine.persistence.entity.ProcessDefinitionEntity;
import com.kaaterskil.workflow.engine.service.ProcessDefinitionService;

public class BpmDeployer implements Deployer {
    private static final Logger log = LoggerFactory.getLogger(BpmDeployer.class);

    private CacheManager cacheManager;

    @Override
    public void deploy(DeploymentEntity deployment) {
        log.debug("Processing deployment {}", deployment.getName());

        final BpmParser parser = new BpmParser(deployment);
        final ParsedDeployment parsedDeployment = parser.execute();

        persistProcessDefinition(parsedDeployment);

        cacheManager.updateCache(parsedDeployment);

        dispatchProcessDefinitionInitializedEvent(parsedDeployment);
    }

    private void persistProcessDefinition(ParsedDeployment parsedDeployment) {
        final CommandContext commandContext = Context.getCommandContext();
        final int version = 1;

        final ProcessDefinitionEntity processDefinition = parsedDeployment.getProcessDefinition();
        processDefinition.setVersion(version);

        final ProcessDefinitionService service = commandContext.getProcessDefinitionService();
        service.save(processDefinition);

        commandContext.getProcessEngineService().getEventDispatcher().dispatchEvent(
                WorkflowEventFactory.createEntityEvent(WorkflowEventType.ENTITY_CREATED,
                        processDefinition));
    }

    private void dispatchProcessDefinitionInitializedEvent(ParsedDeployment parsedDeployment) {
        final CommandContext commandContext = Context.getCommandContext();
        final ProcessDefinitionEntity processDefinition = parsedDeployment.getProcessDefinition();

        commandContext.getProcessEngineService().getEventDispatcher().dispatchEvent(
                WorkflowEventFactory.createEntityEvent(WorkflowEventType.ENTITY_INITIALIZED,
                        processDefinition));
    }

    /*---------- Getter/Setters ----------*/

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

}
