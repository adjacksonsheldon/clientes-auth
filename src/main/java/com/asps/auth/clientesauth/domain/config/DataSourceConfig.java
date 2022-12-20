package com.asps.auth.clientesauth.domain.config;

import com.asps.auth.clientesauth.domain.properties.DataBaseProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@EntityScan(basePackages = {"com.asps.auth.clientesauth.domain.model"})
@EnableJpaRepositories(basePackages = {"com.asps.auth.clientesauth.domain.repository"})
public class DataSourceConfig {

    private final DataBaseProperties dataBaseProperties;

    @Bean("usuariosDataSource")
    @ConfigurationProperties(prefix = "usuarios.datasource")
    public DataSource transactionsBatchDataSource(){
        return DataSourceBuilder.create()
                .url(dataBaseProperties.getUrl())
                .username(dataBaseProperties.getUsername())
                .password(dataBaseProperties.getPassword())
                .build();
    }
}