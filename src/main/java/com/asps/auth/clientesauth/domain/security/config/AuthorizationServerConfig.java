package com.asps.auth.clientesauth.domain.security.config;

import com.asps.auth.clientesauth.domain.security.token.JwtCustomClaimsTokenEnhancer;
import com.asps.auth.clientesauth.domain.security.token.PkceAuthorizationCodeTokenGranter;
import com.asps.auth.clientesauth.domain.security.token.SignerWithAzure;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final SignerWithAzure signer;

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
                .scopes("WRITE", "READ")
                .accessTokenValiditySeconds(60 * 60 * 6)

            .and()
            .withClient("oauth-cliente")
                .secret(passwordEncoder.encode("123"))

            .and()
            .withClient("ms-clientes")
                .secret(passwordEncoder.encode("123"))
                .authorizedGrantTypes("client_credentials")
                .scopes("READ")

            .and()
            .withClient("clientes-spa")
                .secret(passwordEncoder.encode("123"))
                .authorizedGrantTypes("authorization_code")
                .scopes("WRITE", "READ")
                .redirectUris("http://localhost:8080/clientes")

            .and()
            .withClient("clientes-spa-2")
                .authorizedGrantTypes("implicit")
                .scopes("WRITE", "READ")
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
        jwtAccessTokenConverter.setSigner(signer);
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