package com.kaaterskil.workflow.engine.delegate.event;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.kaaterskil.workflow.AbstractUnitTest;
import com.kaaterskil.workflow.bpm.Listener;
import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.bpm.common.process.Process;
import com.kaaterskil.workflow.engine.ProcessEngine;
import com.kaaterskil.workflow.engine.ProcessEngineService;
import com.kaaterskil.workflow.engine.RepositoryService;
import com.kaaterskil.workflow.engine.bpm.ClassDelegate;
import com.kaaterskil.workflow.engine.bpm.DelegateWorkflowEventListener;
import com.kaaterskil.workflow.engine.deploy.DeploymentCache;
import com.kaaterskil.workflow.engine.deploy.ProcessDefinitionCacheEntry;
import com.kaaterskil.workflow.engine.persistence.entity.ProcessDefinitionEntity;

public class TestWorkflowEventListener extends AbstractUnitTest {

    @Autowired
    private ProcessEngineService processEngineService;
    private Process process;
    private ProcessDefinitionEntity processDefinition;

    @BeforeMethod
    public void setUp() {
        final ProcessEngine engine = processEngineService.createProcessEngine();
        final RepositoryService repositoryService = engine.getRepositoryService();

        repositoryService.createDeployment().setName("/test-listeners.xml").deploy();

        final DeploymentCache<ProcessDefinitionCacheEntry> cache = processEngineService
                .getDeploymentService().getProcessDefinitionCache();
        for (final Serializable key : cache.keySet()) {
            final ProcessDefinitionCacheEntry entry = cache.get(key);
            process = entry.getProcess();
            processDefinition = entry.getProcessDefinitionEntity();
        }
    }

    /*---------- Token Listener ----------*/

    @Test
    public void testTokenListenerNotNull() {
        final FlowElement flowElement = process.getFlowElement("task_1", true);
        final List<Listener> listeners = flowElement.getTokenListeners();

        assertNotNull(listeners);
        assertTrue(!listeners.isEmpty());
        assertNotNull(listeners.get(0).getImplementation());
    }

    @Test
    public void testTokenListenerCalled() {
        processEngineService.getRuntimeService().startProcessInstanceByKey("TestEvent");

        final FlowElement flowElement = process.getFlowElement("task_1", true);
        final List<Listener> listeners = flowElement.getTokenListeners();

        assertNotNull(listeners);
        assertTrue(!listeners.isEmpty());

        for (final Listener listener : listeners) {
            assertNotNull(listener.getInstance());

            // The instance would not be a ClassDelegate if it had not been called.
            assertTrue(listener.getInstance() instanceof ClassDelegate);
        }

    }

    /*---------- Process Definition Event Listener ----------*/

    @Test
    public void testEventListenerNotNull() {
        final List<WorkflowEventListener> listeners = processDefinition.getEventHelper()
                .getListeners();
        final List<WorkflowEventListener> typedListeners = processDefinition.getEventHelper()
                .getTypedListeners().get(WorkflowEventType.ENTITY_CREATED);

        assertNotNull(listeners);
        assertTrue(listeners.isEmpty());

        assertNotNull(typedListeners);
        assertTrue(!typedListeners.isEmpty());
        assertNotNull(typedListeners.get(0));
    }

    @Test
    public void testEventListenerCalled() {
        final List<WorkflowEventListener> typedListeners = processDefinition.getEventHelper()
                .getTypedListeners().get(WorkflowEventType.ENTITY_CREATED);

        final DelegateWorkflowEventListener delegateListener = (DelegateWorkflowEventListener) typedListeners
                .get(0);
        assertTrue(((MockEventListener) delegateListener.getDelegateInstance()).getMessage()
                .equals("Event listener called: Entity created"));
    }
}
