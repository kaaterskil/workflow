package com.kaaterskil.workflow.engine;

import static org.testng.Assert.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.kaaterskil.workflow.AbstractUnitTest;
import com.kaaterskil.workflow.engine.ProcessEngine;
import com.kaaterskil.workflow.engine.ProcessEngineService;

public class ProcessEngineServiceTest extends AbstractUnitTest {

    @Autowired
    ProcessEngineService service;

    @Test
    public void testProcessEngineServiceNotNull() {
        assertNotNull(service);
        assertNotNull(service.getRuntimeService());
        assertNotNull(service.getRepositoryService());
        assertNotNull(service.getDeploymentService());
    }

    @Test
    public void testProcessEngineNotNull() {
        final ProcessEngine engine = service.createProcessEngine();
        assertNotNull(engine);
    }

    @Test
    public void testInitialization() {
        service.createProcessEngine();

        assertNotNull(service.getBpmDeployer());
        assertNotNull(service.getCacheManager());
        assertNotNull(service.getCommandExecutor());
        assertNotNull(service.getCommandInvoker());
        assertNotNull(service.getCommandInterceptors());
        assertNotNull(service.getProcessDefinitionCache());
    }

}
