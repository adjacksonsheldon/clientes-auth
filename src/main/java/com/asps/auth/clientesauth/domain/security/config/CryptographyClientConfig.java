package com.asps.auth.clientesauth.domain.security.config;

import com.asps.auth.clientesauth.infrastructure.properties.KeyVaultProperties;
import com.azure.identity.ClientSecretCredential;
import com.azure.security.keyvault.keys.cryptography.CryptographyClient;
import com.azure.security.keyvault.keys.cryptography.CryptographyClientBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@Getter
public class CryptographyClientConfig {

    private final ClientSecretCredential credentials;
    private final KeyVaultProperties keyVaultProperties;

    @Bean
    public CryptographyClient cryptographyClient() {
        CryptographyClient cryptographyClient = new CryptographyClientBuilder()
                .keyIdentifier(keyVaultProperties.getIdentifier())
                .credential(credentials)
                .buildClient();

        return cryptographyClient;
    }
}