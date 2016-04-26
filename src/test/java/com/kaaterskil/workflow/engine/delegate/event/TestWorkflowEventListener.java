package com.kaaterskil.workflow.engine.delegate.event;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.kaaterskil.workflow.AbstractUnitTest;
import com.kaaterskil.workflow.bpm.Listener;
import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.engine.ProcessEngine;
import com.kaaterskil.workflow.engine.ProcessEngineService;
import com.kaaterskil.workflow.engine.RepositoryService;
import com.kaaterskil.workflow.engine.bpm.ClassDelegate;
import com.kaaterskil.workflow.engine.bpm.DelegateWorkflowEventListener;
import com.kaaterskil.workflow.engine.deploy.ProcessDefinitionCacheEntry;
import com.kaaterskil.workflow.engine.runtime.ProcessInstance;

public class TestWorkflowEventListener extends AbstractUnitTest {

    @Autowired
    private ProcessEngineService processEngineService;

    @BeforeMethod
    public void setUp() {
        final ProcessEngine engine = processEngineService.createProcessEngine();
        final RepositoryService repositoryService = engine.getRepositoryService();

        repositoryService.createDeployment().setName("/test-listeners.xml").deploy();
    }

    /*---------- Token Listener ----------*/

    @Test
    public void testTokenListenerNotNull() {
        final ProcessInstance processInstance = processEngineService.getRuntimeService()
                .startProcessInstanceByKey("TestEvent");
        final ProcessDefinitionCacheEntry entry = processEngineService.getDeploymentService()
                .getProcessDefinitionCache().get(processInstance.getProcessDefinitionId());

        final FlowElement flowElement = entry.getBpmModel().getProcess().getFlowElement("task_1",
                true);
        final List<Listener> listeners = flowElement.getTokenListeners();

        assertNotNull(listeners);
        assertTrue(!listeners.isEmpty());
        assertNotNull(listeners.get(0).getImplementation());
    }

    @Test
    public void testTokenListenerCalled() {
        final ProcessInstance processInstance = processEngineService.getRuntimeService()
                .startProcessInstanceByKey("TestEvent");
        final ProcessDefinitionCacheEntry entry = processEngineService.getDeploymentService()
                .getProcessDefinitionCache().get(processInstance.getProcessDefinitionId());

        final FlowElement flowElement = entry.getBpmModel().getProcess().getFlowElement("task_1",
                true);
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
        final ProcessInstance processInstance = processEngineService.getRuntimeService()
                .startProcessInstanceByKey("TestEvent");
        final ProcessDefinitionCacheEntry entry = processEngineService.getDeploymentService()
                .getProcessDefinitionCache().get(processInstance.getProcessDefinitionId());

        final List<WorkflowEventListener> listeners = entry.getProcessDefinitionEntity()
                .getEventHelper().getListeners();
        final List<WorkflowEventListener> typedListeners = entry.getProcessDefinitionEntity()
                .getEventHelper().getTypedListeners().get(WorkflowEventType.ENTITY_CREATED);

        assertNotNull(listeners);
        assertTrue(listeners.isEmpty());

        assertNotNull(typedListeners);
        assertTrue(!typedListeners.isEmpty());
        assertNotNull(typedListeners.get(0));
    }

    @Test
    public void testEventListenerCalled() {
        final ProcessInstance processInstance = processEngineService.getRuntimeService()
                .startProcessInstanceByKey("TestEvent");
        final ProcessDefinitionCacheEntry entry = processEngineService.getDeploymentService()
                .getProcessDefinitionCache().get(processInstance.getProcessDefinitionId());

        final List<WorkflowEventListener> typedListeners = entry.getProcessDefinitionEntity()
                .getEventHelper().getTypedListeners().get(WorkflowEventType.ENTITY_CREATED);

        final DelegateWorkflowEventListener delegateListener = (DelegateWorkflowEventListener) typedListeners
                .get(0);
        assertTrue(((MockEventListener) delegateListener.getDelegateInstance()).getMessage()
                .equals("Event listener called: Entity created"));
    }
}
