package com.asps.auth.clientesauth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class AppConfig {
    @Value("${signingKey}")
    private String signingKey;
}
