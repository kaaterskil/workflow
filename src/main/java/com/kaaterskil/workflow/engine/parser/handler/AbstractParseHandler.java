package com.kaaterskil.workflow.engine.parser.handler;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.kaaterskil.workflow.bpm.ImplementationType;
import com.kaaterskil.workflow.bpm.Listener;
import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.bpm.foundation.BaseElement;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.TokenListener;
import com.kaaterskil.workflow.engine.parser.BpmParser;
import com.kaaterskil.workflow.engine.parser.ListenerFactory;

public abstract class AbstractParseHandler<T extends BaseElement> implements ParseHandler {

    @Override
    public Collection<Class<? extends BaseElement>> getHandledTypes() {
        final Set<Class<? extends BaseElement>> types = new HashSet<>();
        types.add(getHandledType());
        return types;
    }

    protected abstract Class<? extends BaseElement> getHandledType();

    @SuppressWarnings("unchecked")
    @Override
    public void parse(BpmParser parser, BaseElement target) {
        final T element = (T) target;
        executeParse(parser, element);
    }

    protected abstract void executeParse(BpmParser parser, T element);

    protected void createTokenListeners(BpmParser parser, FlowElement flowElement) {

    }

    protected TokenListener createTokenListener(BpmParser parser, Listener listener) {
        final ListenerFactory listenerFactory = Context.getProcessEngineService()
                .getListenerFactory();
        TokenListener tokenListener = null;

        if (listener.getImplementationType() != null
                && listener.getImplementationType().equals(ImplementationType.CLASS)) {
            tokenListener = listenerFactory.createClassDelegateTokenListener(listener);
        }
        return tokenListener;
    }

}
