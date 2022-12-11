package com.asps.auth.clientesauth.core;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;

public class JwtCustomClaimsTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        if(isUsuarioFinalAutenticado(authentication)){
            final var authUser = (AuthUser) authentication.getPrincipal();

            final var info = new HashMap<String, Object>();
            info.put("nome_completo", authUser.getFullName());
            info.put("usuario_id", authUser.getUsuarioId());

            final var defaultOAuth2AccessToken = (DefaultOAuth2AccessToken) accessToken;
            defaultOAuth2AccessToken.setAdditionalInformation(info);
        }

        return accessToken;
    }

    private boolean isUsuarioFinalAutenticado(OAuth2Authentication authentication) {
        return authentication.getPrincipal() instanceof AuthUser;
    }
}
