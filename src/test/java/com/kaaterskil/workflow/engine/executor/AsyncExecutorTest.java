package com.kaaterskil.workflow.engine.executor;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.kaaterskil.workflow.AbstractUnitTest;
import com.kaaterskil.workflow.engine.ProcessEngineService;

public class AsyncExecutorTest extends AbstractUnitTest {

    @Autowired
    private ProcessEngineService processEngineService;
    private AsyncExecutor executor;

    @BeforeMethod
    public void setUp() {
        processEngineService.createProcessEngine();
        executor = processEngineService.getAsyncExecutor();
    }

    @AfterMethod
    public void tearDown() {
        executor.shutdown();
        executor = null;
    }

    @Test
    public void testExecutorNotNull() {
        final BaseAsyncExecutor asyncExecutor = (BaseAsyncExecutor) executor;

        assertNotNull(asyncExecutor);
        assertNotNull(asyncExecutor.getCommandExecutor());
        assertNotNull(asyncExecutor.getTemporaryJobQueue());
    }

    @Test
    public void testIsActive() {
        final BaseAsyncExecutor asyncExecutor = (BaseAsyncExecutor) executor;

        assertFalse(asyncExecutor.isActive());

        asyncExecutor.start();
        assertNotNull(asyncExecutor.getExecutorService());
        assertNotNull(asyncExecutor.getThreadPoolQueue());

        assertTrue(asyncExecutor.isActive());
    }
}
