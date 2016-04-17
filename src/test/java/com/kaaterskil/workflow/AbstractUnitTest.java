package com.kaaterskil.workflow;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;

@ActiveProfiles("workflow-test")
@SpringApplicationConfiguration(WorkflowApplication.class)
public class AbstractUnitTest extends AbstractTransactionalTestNGSpringContextTests {

    // Noop
}
