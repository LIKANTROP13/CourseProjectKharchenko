package com.example.courseproject.config.database;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.diet")
    public DataSource dietDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public JdbcTemplate dietJdbcTemplate() {
        return new JdbcTemplate(dietDataSource());
    }
}
