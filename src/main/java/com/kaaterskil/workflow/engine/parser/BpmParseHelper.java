package com.kaaterskil.workflow.engine.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kaaterskil.workflow.bpm.foundation.BaseElement;
import com.kaaterskil.workflow.engine.parser.handler.ParseHandler;

public class BpmParseHelper {

    private Map<Class<? extends BaseElement>, List<ParseHandler>> parseHandlers = new HashMap<>();

    public List<ParseHandler> getHandlersForType(Class<? extends BaseElement> type) {
        return parseHandlers.get(type);
    }

    public void addHandlers(List<ParseHandler> handlers) {
        if (handlers != null && !handlers.isEmpty()) {
            for (final ParseHandler handler : handlers) {
                addHandler(handler);
            }
        }
    }

    public void addHandler(ParseHandler handler) {
        for (final Class<? extends BaseElement> type : handler.getHandledTypes()) {
            List<ParseHandler> handlers = parseHandlers.get(type);
            if (handlers == null) {
                handlers = new ArrayList<ParseHandler>();
                parseHandlers.put(type, handlers);
            }
            handlers.add(handler);
        }
    }

    public void parseElement(BpmParser parser, BaseElement element) {
        final List<ParseHandler> handlers = parseHandlers.get(element.getClass());
        if (handlers != null) {
            for (final ParseHandler handler : handlers) {
                handler.parse(parser, element);
            }
        }
    }

    /*---------- Getter/Setters ----------*/

    public Map<Class<? extends BaseElement>, List<ParseHandler>> getParseHandlers() {
        return parseHandlers;
    }

    public void setParseHandlers(
            Map<Class<? extends BaseElement>, List<ParseHandler>> parseHandlers) {
        this.parseHandlers = parseHandlers;
    }
}
