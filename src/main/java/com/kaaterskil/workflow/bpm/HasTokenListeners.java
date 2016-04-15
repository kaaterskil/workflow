package com.kaaterskil.workflow.bpm;

import java.util.List;

public interface HasTokenListeners {

    List<Listener> getTokenListeners();

    void setTokenListeners(List<Listener> tokenListeners);
}
