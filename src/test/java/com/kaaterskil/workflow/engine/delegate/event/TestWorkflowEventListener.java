package com.kaaterskil.workflow.engine.delegate.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.kaaterskil.workflow.AbstractUnitTest;
import com.kaaterskil.workflow.engine.ProcessEngine;
import com.kaaterskil.workflow.engine.ProcessEngineService;
import com.kaaterskil.workflow.engine.RepositoryService;

public class TestWorkflowEventListener extends AbstractUnitTest {

    @Autowired
    private ProcessEngineService processEngineService;

    @BeforeMethod
    public void setUp() {
        final ProcessEngine engine = processEngineService.createProcessEngine();
        final RepositoryService repositoryService = engine.getRepositoryService();

        repositoryService.createDeployment().setName("/test-event-listener.xml").deploy();
    }

    @Test
    public void testEventListenerNotNull() {
    }
}
