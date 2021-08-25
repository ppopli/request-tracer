package com.pulkit.requesttracer.datasource.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(value = "request.tracing.enabled")
@EnableJpaRepositories(
        basePackages = "com.pulkit.requesttracer.datasource.repository.master"
)
@EntityScan(basePackages = "com.pulkit.requesttracer.datasource.entity")
public class DataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "master.logging.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }
}
