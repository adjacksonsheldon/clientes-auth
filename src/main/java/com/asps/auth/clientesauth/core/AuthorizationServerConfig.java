package com.asps.auth.clientesauth.core;

import com.asps.auth.clientesauth.config.AppConfig;
import com.asps.auth.clientesauth.config.JwtAccessTokenProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
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
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final AppConfig appConfig;

    private final JwtAccessTokenProperties jwtAccessTokenProperties;

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

        final var enhancerChain = new TokenEnhancerChain();
        enhancerChain.setTokenEnhancers(List.of(new JwtCustomClaimsTokenEnhancer(), jwtAccessTokenConverter()));

        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenGranter(tokenGranter(endpoints))
                .reuseRefreshTokens(false)
                .accessTokenConverter(jwtAccessTokenConverter())
                .approvalStore(getTokenApprovalStore(endpoints))
                .tokenEnhancer(enhancerChain);
    }

    private static TokenApprovalStore getTokenApprovalStore(AuthorizationServerEndpointsConfigurer endpoints) {
        final var approvalStore = new TokenApprovalStore();
        approvalStore.setTokenStore(endpoints.getTokenStore());
        return approvalStore;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.checkTokenAccess("isAuthenticated()")
                .tokenKeyAccess("permitAll()");
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        final var jwtAccessTokenConverter = new JwtAccessTokenConverter();
//        jwtAccessTokenConverter.setSigningKey(appConfig.getSigningKey());

        final var jksResource = new ClassPathResource(jwtAccessTokenProperties.getPath());
        final var keyStoreKeyFactory = new KeyStoreKeyFactory(jksResource, jwtAccessTokenProperties.getPassword().toCharArray());
        final var keyPair = keyStoreKeyFactory.getKeyPair(jwtAccessTokenProperties.getAlias());
        jwtAccessTokenConverter.setKeyPair(keyPair);

        return jwtAccessTokenConverter;
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