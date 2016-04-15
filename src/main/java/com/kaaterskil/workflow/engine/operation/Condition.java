package com.kaaterskil.workflow.engine.operation;

import com.kaaterskil.workflow.engine.delegate.DelegateToken;

public interface Condition {

    boolean evaluate(DelegateToken token);
}
