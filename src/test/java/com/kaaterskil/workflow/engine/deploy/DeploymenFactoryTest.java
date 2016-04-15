package com.kaaterskil.workflow.engine.deploy;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.kaaterskil.workflow.AbstractUnitTest;
import com.kaaterskil.workflow.engine.ProcessEngineService;
import com.kaaterskil.workflow.engine.persistence.entity.DeploymentEntity;

public class DeploymenFactoryTest extends AbstractUnitTest {

    @Autowired
    private ProcessEngineService processEngineService;
    private DeploymentFactory factory;
    private final String deploymentName = "/test-process.xml";

    @BeforeMethod
    public void setUp() {
        processEngineService.createProcessEngine();
        factory = processEngineService.getRepositoryService().createDeployment();
        factory.setName(deploymentName);
    }

    @Test
    public void testDeploymentNotNull() {
        assertNotNull(factory.getDeployment());
        assertTrue(factory.getName().equals(deploymentName));
    }

    @Test
    public void testDeploy() {
        final DeploymentEntity deployment = factory.deploy();
        System.out.println(deployment.toString());
        assertNotNull(deployment);
    }
}
