package com.kaaterskil.workflow;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.kaaterskil.workflow.engine.util.ApplicationContextUtil;

@Configuration
@ComponentScan(basePackages = {
        "com.kaaterskil.workflow.engine", "com.kaaterskil.workflow.engine.delegate.event",
        "com.kaaterskil.workflow.engine.mapper", "com.kaaterskil.workflow.engine.interceptor",
        "com.kaaterskil.workflow.engine.service.impl", "com.kaaterskil.workflow.impl.condition" })
public class WorkflowConfiguration {

    @Bean
    public ApplicationContextUtil applicationContextUtil() {
        return new ApplicationContextUtil();
    }
}
