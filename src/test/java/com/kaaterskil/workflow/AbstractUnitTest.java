package com.kaaterskil.workflow;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;

@SpringApplicationConfiguration(classes = {
        WorkflowConfiguration.class, DomainTestConfiguration.class })
public class AbstractUnitTest extends AbstractTransactionalTestNGSpringContextTests {

    // Noop
}
