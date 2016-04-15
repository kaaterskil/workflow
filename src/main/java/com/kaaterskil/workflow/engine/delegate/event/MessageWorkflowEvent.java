package com.kaaterskil.workflow.engine.delegate.event;

public class MessageWorkflowEvent extends ActivityWorkflowEvent {

    protected String messageName;
    protected Object payload;

    public MessageWorkflowEvent(WorkflowEventType type) {
        super(type);
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
