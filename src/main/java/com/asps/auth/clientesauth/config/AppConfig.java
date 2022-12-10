package com.asps.auth.clientesauth.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AppConfig {

    @Value("${CLIENTE_ID}")
    private String clientId;

    @Value("${CLIENTE_SECRET}")
    private String clientSecret;

    @Value("${TENANT_ID}")
    private String tenantId;
}
