package com.asps.auth.clientesauth.domain.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("database.properties")
public class DataBaseProperties {
    private String username;
    private String password;
    private String url;
}
