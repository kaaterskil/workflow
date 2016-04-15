package com.kaaterskil.workflow.engine.parser;

import com.kaaterskil.workflow.bpm.Listener;
import com.kaaterskil.workflow.engine.delegate.TokenListener;

public interface ListenerFactory {

    TokenListener createClassDelegateTokenListener(Listener listener);
}
