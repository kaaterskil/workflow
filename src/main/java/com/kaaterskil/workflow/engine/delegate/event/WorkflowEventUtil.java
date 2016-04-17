package com.kaaterskil.workflow.engine.delegate.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kaaterskil.workflow.engine.exception.NullValueException;

public class WorkflowEventUtil {

    private final List<WorkflowEventListener> listeners = new ArrayList<>();
    private final Map<WorkflowEventType, List<WorkflowEventListener>> typedListeners = new HashMap<>();

    public synchronized void addEventListener(WorkflowEventListener listener) {
        if (listener == null) {
            throw new NullValueException("listener cannot be null");
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public synchronized void addEventListener(WorkflowEventListener listener,
            WorkflowEventType... eventTypes) {
        if (listener == null) {
            throw new NullValueException("listener cannot be null");
        }
        if (eventTypes == null || eventTypes.length == 0) {
            addEventListener(listener);
        } else {
            for (final WorkflowEventType type : eventTypes) {
                addTypedEventListener(listener, type);
            }
        }
    }

    public void removeEventListener(WorkflowEventListener listener) {
        listeners.remove(listener);
        for(final List<WorkflowEventListener> list : typedListeners.values()) {
            list.remove(listener);
        }
    }

    public void dispatchEvent(WorkflowEvent event) {
        if(event == null) {
            throw new NullValueException("event cannot be null");
        }

        // Call global listeners
        if(!listeners.isEmpty()) {
            for(final WorkflowEventListener listener : listeners) {
                dispatchEvent(event, listener);
            }
        }

        // Call typed listeners
        final List<WorkflowEventListener> list = typedListeners.get(event.getType());
        if(list != null && !list.isEmpty()) {
            for(final WorkflowEventListener listener : list) {
                dispatchEvent(event, listener);
            }
        }
    }

    protected void dispatchEvent(WorkflowEvent event, WorkflowEventListener listener) {
        listener.onEvent(event);
    }

    public synchronized void addTypedEventListener(WorkflowEventListener listener,
            WorkflowEventType type) {
        List<WorkflowEventListener> list = typedListeners.get(type);
        if (list == null) {
            list = new ArrayList<WorkflowEventListener>();
            typedListeners.put(type, list);
        }
        if (!list.contains(listener)) {
            list.add(listener);
        }
    }

    public List<WorkflowEventListener> getListeners() {
        return listeners;
    }

    public Map<WorkflowEventType, List<WorkflowEventListener>> getTypedListeners() {
        return typedListeners;
    }
}
