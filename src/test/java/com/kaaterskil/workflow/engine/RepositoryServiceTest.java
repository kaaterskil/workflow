package com.kaaterskil.workflow.engine;

import static org.testng.Assert.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.kaaterskil.workflow.AbstractUnitTest;

public class RepositoryServiceTest extends AbstractUnitTest {

    @Autowired
    private ProcessEngineService processEngineService;
    private RepositoryService service;

    @BeforeMethod
    public void setUp() {
        processEngineService.createProcessEngine();
        service = processEngineService.getRepositoryService();
    }

    @Test
    public void testConfiguration() {
        assertNotNull(service.getCommandExecutor());
        assertNotNull(service.getDeploymentService());
        assertNotNull(service.getProcessDefinitionService());
    }

    @Test
    public void testCreateDeploymentFactoryNotNull() {
        assertNotNull(service.createDeployment());
    }
}
