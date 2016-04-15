package com.kaaterskil.workflow.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.engine.interceptor.CommandExecutor;

public class ProcessEngine {
    private static final Logger log = LoggerFactory.getLogger(ProcessEngine.class);

    private final ProcessEngineService processEngineService;
    private final RuntimeService runtimeService;
    private final CommandExecutor commandExecutor;

    public ProcessEngine(ProcessEngineService processEngineService) {
        this.processEngineService = processEngineService;
        runtimeService = processEngineService.getRuntimeService();
        commandExecutor = processEngineService.getCommandExecutor();

        log.debug("Process Engine created");
    }

    public ProcessEngineService getProcessEngineService() {
        return processEngineService;
    }

    public RuntimeService getRuntimeService() {
        return runtimeService;
    }

    public CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }
}
