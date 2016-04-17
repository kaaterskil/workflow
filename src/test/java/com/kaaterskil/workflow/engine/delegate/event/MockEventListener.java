package com.kaaterskil.workflow.engine.delegate.event;

import org.springframework.stereotype.Component;

@Component
public class MockEventListener implements WorkflowEventListener {

    private String message;

    @Override
    public void onEvent(WorkflowEvent event) {
        final StringBuffer sb = new StringBuffer("Event listener called: ");

        switch (event.getType()) {
        case ACTIVITY_STARTED:
            sb.append("Activity started");
            break;
        case ENTITY_CREATED:
            sb.append("Entity created");
            break;
        case ENTITY_INITIALIZED:
            sb.append("Entity initialized");
            break;
        default:
            sb.append("Some other outcome: " + event.getType());
            break;
        }

        message = sb.toString();
        System.out.println(message);
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }

    @Override
    public String toString() {
        return String.format("MockEventListener [message=%s]", message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
