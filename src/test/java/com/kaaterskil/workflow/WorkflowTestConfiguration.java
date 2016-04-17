package com.kaaterskil.workflow;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.kaaterskil.workflow.engine.util.ApplicationContextUtil;

@Profile("workflow-test")
@Configuration
@ComponentScan
public class WorkflowTestConfiguration {

    @Bean
    public ApplicationContextUtil applicationContextUtil() {
        return new ApplicationContextUtil();
    }
}
