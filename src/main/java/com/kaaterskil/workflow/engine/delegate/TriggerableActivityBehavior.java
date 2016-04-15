package com.kaaterskil.workflow.engine.delegate;

public interface TriggerableActivityBehavior extends ActivityBehavior {

    void trigger(DelegateToken token, String signalEvent, Object signalData);
}
