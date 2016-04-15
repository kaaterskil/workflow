package com.kaaterskil.workflow;

import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.kaaterskil.workflow.engine.persistence.repository")
@EntityScan(basePackages = "com.kaaterskil.workflow.engine.persistence.entity")
@Import({ DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class DomainTestConfiguration {

}
