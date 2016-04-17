package com.kaaterskil.workflow.engine.parser.factory;

import com.kaaterskil.workflow.bpm.Listener;
import com.kaaterskil.workflow.engine.delegate.TokenListener;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventListener;

public interface ListenerFactory {

    TokenListener createClassDelegateTokenListener(Listener listener);

    WorkflowEventListener createClassDelegateEventListener(Listener listener);
}
