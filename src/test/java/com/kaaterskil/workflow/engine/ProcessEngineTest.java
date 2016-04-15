package com.kaaterskil.workflow.engine;

import static org.testng.Assert.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.kaaterskil.workflow.AbstractUnitTest;

public class ProcessEngineTest extends AbstractUnitTest {

    @Autowired
    private ProcessEngineService processEngineService;

    private ProcessEngine engine;

    @BeforeMethod
    public void SetUp() {
        engine = processEngineService.createProcessEngine();
    }

    @Test
    public void testEngineConfiguration() {
        assertNotNull(engine.getProcessEngineService());
        assertNotNull(engine.getRuntimeService());
        assertNotNull(engine.getCommandExecutor());
    }
}
