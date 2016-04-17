package com.kaaterskil.workflow.engine.parser.handler;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.kaaterskil.workflow.bpm.foundation.BaseElement;
import com.kaaterskil.workflow.engine.parser.BpmParser;

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

}
