package com.asps.auth.clientesauth.config;

import com.azure.identity.ClientSecretCredential;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@Getter
@Setter
public class KeyPairConfig {

    private final ClientSecretCredential credentials;
//
//    @Bean
//    public void getKeyPair(){
//
//        CryptographyClient keyClient = new CryptographyClientBuilder()
//                .keyIdentifier(privateKey())
//                .credential(credentials)
//                .buildClient();
//
//        keyClient.getKey().getKey().toRsa(true);
//    }
//
//    private String privateKey(){

//        String keyVaultUri = "https://" + keyVaultName + ".vault.azure.net";
//
//
//        KeyClient keyClient = new KeyClientBuilder()
//                .vaultUrl(keyVaultUri)
//                .credential(credentials)
//                .buildClient();
//
//        return keyClient.getKey("keyVaultName").getKey().getId();
//    }
}
