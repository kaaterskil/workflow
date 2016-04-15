package com.kaaterskil.workflow.engine.delegate.event;

public class SignalWorkflowEvent extends ActivityWorkflowEvent {

    protected String signalName;
    protected Object signalData;

    public SignalWorkflowEvent(WorkflowEventType type) {
        super(type);
    }

    public String getSignalName() {
        return signalName;
    }

    public void setSignalName(String signalName) {
        this.signalName = signalName;
    }

    public Object getSignalData() {
        return signalData;
    }

    public void setSignalData(Object signalData) {
        this.signalData = signalData;
    }
}
