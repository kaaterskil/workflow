package com.kaaterskil.workflow.engine.deploy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.bpm.common.process.Process;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.persistence.entity.ProcessDefinitionEntity;

public class CacheManager {
    private static final Logger log = LoggerFactory.getLogger(CacheManager.class);

    public void updateCache(ParsedDeployment parsedDeployment) {
        final DeploymentCache<ProcessDefinitionCacheEntry> cache = Context
                .getCommandContext().getDeploymentService().getProcessDefinitionCache();

        log.debug("Updating cache for for deployment {}",
                parsedDeployment.getDeployment().getName());

        final ProcessDefinitionEntity processDefinition = parsedDeployment.getProcessDefinition();
        final Process process = parsedDeployment.getProcess();
        final String xml = parsedDeployment.getXml();

//        System.out.println(processDefinition.toString());
//        System.out.println(process.toString());
//        System.out.println(xml);

        final ProcessDefinitionCacheEntry cacheEntry = new ProcessDefinitionCacheEntry(
                processDefinition, process, xml);
        cache.put(processDefinition.getId(), cacheEntry);
    }
}
