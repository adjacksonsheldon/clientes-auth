package com.asps.auth.clientesauth.config;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ClientCredentialsConfig {

    private final AppConfig appConfig;

    @Bean
    public ClientSecretCredential getCredentials() {
        ClientSecretCredential secondServicePrincipal = new ClientSecretCredentialBuilder()
                .clientId(appConfig.getClientId())
                .clientSecret(appConfig.getClientSecret())
                .tenantId(appConfig.getTenantId())
                .build();

        return secondServicePrincipal;
    }
}