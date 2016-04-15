package com.kaaterskil.workflow.engine.interceptor;

import com.kaaterskil.workflow.engine.ProcessEngineService;

public class CommandContextFactory {

    private ProcessEngineService processEngineService;

    public CommandContext createInstance() {
        return new CommandContext(processEngineService);
    }

    /*---------- Getter/Setters ----------*/

    public ProcessEngineService getProcessEngineService() {
        return processEngineService;
    }

    public void setProcessEngineService(ProcessEngineService processEngineService) {
        this.processEngineService = processEngineService;
    }
}
