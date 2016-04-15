package com.kaaterskil.workflow.engine.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.engine.command.Command;

public class LogInterceptor extends CommandInterceptor {
    private static final Logger log = LoggerFactory.getLogger(LogInterceptor.class);

    @Override
    public <T> T execute(Command<T> command) {
        log.debug("Starting command {}", command.getClass().getSimpleName());

        try {
            return next.execute(command);
        } finally {
            log.debug("Finishing command {}", command.getClass().getSimpleName());
        }
    }

}
