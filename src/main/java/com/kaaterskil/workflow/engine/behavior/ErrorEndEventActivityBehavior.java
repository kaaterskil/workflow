package com.kaaterskil.workflow.engine.behavior;

import com.kaaterskil.workflow.engine.delegate.DelegateToken;

public class ErrorEndEventActivityBehavior extends FlowNodeActivityBehavior {

    private String errorRef;

    public ErrorEndEventActivityBehavior(String errorRef) {
        this.errorRef = errorRef;
    }

    @Override
    public void execute(DelegateToken token) {
        // TODO Auto-generated method stub
        super.execute(token);
    }

    public String getErrorRef() {
        return errorRef;
    }

    public void setErrorRef(String errorRef) {
        this.errorRef = errorRef;
    };
}
