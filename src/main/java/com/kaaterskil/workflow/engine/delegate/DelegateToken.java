package com.kaaterskil.workflow.engine.delegate;

import java.util.List;

import com.kaaterskil.workflow.bpm.common.FlowElement;

public interface DelegateToken extends VariableScope {

    Long getId();

    Long getProcessInstanceId();

    Long getRootProcessInstanceId();

    Long getProcessDefinitionId();

    Long getParentId();

    String getCurrentActivityId();

    FlowElement getCurrentFlowElement();

    void setCurrentFlowElement(FlowElement currentFlowElement);

    DelegateToken getParent();

    List<? extends DelegateToken> getChildTokens();

    boolean isActive();

    void setActive(boolean isActive);

    boolean isEnded();

    boolean isScope();

    void setScope(boolean isScope);

    boolean isProcessInstanceType();
}
