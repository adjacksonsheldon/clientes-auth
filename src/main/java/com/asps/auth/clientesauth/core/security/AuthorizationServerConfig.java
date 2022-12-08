package com.asps.auth.clientesauth.core.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final RedisConnectionFactory redisConnectionFactory;

    /**
     * Essa configuração é utilizada personalizar a aplicação cliente do Authorization Server
     *
     * @param clients the client details configurer
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
            .withClient("clientes-front")
                .secret(passwordEncoder.encode("123"))
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("write", "read")
                .accessTokenValiditySeconds(60 * 60 * 6)

            .and()
            .withClient("oauth-cliente")
                .secret(passwordEncoder.encode("123"))

            .and()
            .withClient("ms-clientes")
                .secret(passwordEncoder.encode("123"))
                .authorizedGrantTypes("client_credentials")
                .scopes("read")

            // http://localhost:8081/oauth/authorize?response_type=code&client_id=clientes-spa&state=abc&redirect_uri=http://localhost:8080/clientes&code_challenge=teste123&code_challenge_method=plain
            //http://localhost:8081/oauth/authorize?response_type=code&client_id=clientes-spa&redirect_uri=http://localhost:8080/clientes&code_challenge=_zFzot42nm_C_g93eIHuKxEfo1iRYqYk6ifxncemqXM&code_challenge_method=s256
            .and()
            .withClient("clientes-spa")
                .secret(passwordEncoder.encode("123"))
                .authorizedGrantTypes("authorization_code")
                .scopes("write", "read")
                .redirectUris("http://localhost:8080/clientes")

            // http://localhost:8081/oauth/authorize?response_type=token&client_id=clientes-spa-2&state=abc&redirect_uri=http://localhost:8080/clientes
            .and()
            .withClient("clientes-spa-2")
                .authorizedGrantTypes("implicit")
                .scopes("write", "read")
                .redirectUris("http://localhost:8080/clientes")
        ;
    }

    /**
     * Especificamos o authenticationManager para que o Authorization Server valide a senha do usuário final para o fluxo Password Credentials
     *
     * @param endpoints the endpoints configurer
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenGranter(tokenGranter(endpoints))
                .reuseRefreshTokens(false)
                .tokenStore(redisTokenStore());
    }

    private TokenStore redisTokenStore(){
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.checkTokenAccess("isAuthenticated()");
    }

    private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
        var pkceAuthorizationCodeTokenGranter = new PkceAuthorizationCodeTokenGranter(endpoints.getTokenServices(),
                endpoints.getAuthorizationCodeServices(), endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory());

        var granters = Arrays.asList(
                pkceAuthorizationCodeTokenGranter, endpoints.getTokenGranter());

        return new CompositeTokenGranter(granters);
    }
}