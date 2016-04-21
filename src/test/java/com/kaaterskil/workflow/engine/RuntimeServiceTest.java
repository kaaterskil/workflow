package com.kaaterskil.workflow.engine;

import static org.testng.Assert.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.kaaterskil.workflow.AbstractUnitTest;
import com.kaaterskil.workflow.engine.runtime.ProcessInstance;

public class RuntimeServiceTest extends AbstractUnitTest {

    @Autowired
    private ProcessEngineService processEngineService;
    RuntimeService runtimeService;

    @BeforeMethod
    public void setUp() {
        final ProcessEngine engine = processEngineService.createProcessEngine();
        final RepositoryService repositoryService = engine.getRepositoryService();

        repositoryService.createDeployment().setName("/test-process.xml").deploy();
        runtimeService = engine.getRuntimeService();
    }

    @Test
    public void testStartProcessInstance() {
        final ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("helloWorld");

        assertNotNull(processInstance);
    }
}
