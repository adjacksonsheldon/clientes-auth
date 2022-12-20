package com.asps.auth.clientesauth.infrastructure.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("keyvault.properties")
public class KeyVaultProperties {

    private String clientId;

    private String clientSecret;

    private String tenantId;

    private String identifier;
}
