package com.kaaterskil.workflow.engine.parser.handler;

import java.util.Collection;

import com.kaaterskil.workflow.bpm.foundation.BaseElement;
import com.kaaterskil.workflow.engine.parser.BpmParser;

public interface ParseHandler {

    Collection<Class<? extends BaseElement>> getHandledTypes();

    void parse(BpmParser parser, BaseElement target);
}
