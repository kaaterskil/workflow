package com.kaaterskil.workflow.engine.delegate.event;

public class BaseWorkflowEvent implements WorkflowEvent {

    protected WorkflowEventType type;
    protected Long tokenId;
    protected Long processInstanceId;
    protected Long processDefinitionId;

    public BaseWorkflowEvent(WorkflowEventType type) {
        this(type, null, null, null);
    }

    public BaseWorkflowEvent(WorkflowEventType type, Long tokenId,
            Long processInstanceId, Long processDefinitionId) {
        this.type = type;
        this.tokenId = tokenId;
        this.processInstanceId = processInstanceId;
        this.processDefinitionId = processDefinitionId;
    }

    @Override
    public WorkflowEventType getType() {
        return type;
    }

    public void setType(WorkflowEventType type) {
        this.type = type;
    }

    @Override
    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    @Override
    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @Override
    public Long getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(Long processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }
}
