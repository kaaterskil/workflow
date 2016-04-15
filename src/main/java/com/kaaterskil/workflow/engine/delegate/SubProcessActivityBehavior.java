package com.kaaterskil.workflow.engine.delegate;

public interface SubProcessActivityBehavior extends ActivityBehavior {

    /**
     * Called before the process instance is destroyed to allow this activity to extract data from
     * the subprocess instance. No control flow logic should be performed yet.
     *
     * @param token
     * @param subProcessToken
     * @throws Exception
     */
    void completing(DelegateToken token, DelegateToken subProcessToken) throws Exception;

    /**
     * Called after the process instance is destroyed. The activity should perform its outgoing
     * control flow logic.
     *
     * @param token
     * @throws Exception
     */
    void completed(DelegateToken token) throws Exception;
}
