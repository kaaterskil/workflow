package com.kaaterskil.workflow.engine.delegate.event;

import org.springframework.stereotype.Component;

@Component
public class MockEventListener implements WorkflowEventListener {

    @Override
    public void onEvent(WorkflowEvent event) {
        switch (event.getType()) {
        case JOB_EXECUTION_SUCCESS:
            System.out.println("Job succeeded");
            break;
        case JOB_EXECUTION_FAILURE:
            System.out.println("Job failed");
            break;
        default:
            System.out.println("Some other outcome: " + event.getType());
            break;
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }

    @Override
    public String toString() {
        return String.format("MockEventListener []");
    }

}
