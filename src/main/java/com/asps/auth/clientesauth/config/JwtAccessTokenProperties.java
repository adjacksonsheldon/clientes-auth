package com.asps.auth.clientesauth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties("token.properties")
public class JwtAccessTokenProperties {

    private String path;
    private String password;
    private String alias;
}
