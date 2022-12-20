package com.asps.auth.clientesauth.infrastructure.config;

import com.azure.security.keyvault.keys.cryptography.CryptographyClient;
import com.azure.security.keyvault.keys.cryptography.models.SignResult;
import com.azure.security.keyvault.keys.cryptography.models.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RequiredArgsConstructor
@Component
public class SignerWithAzure implements org.springframework.security.jwt.crypto.sign.Signer {

    private final CryptographyClient cryptographyClient;

    @Override
    public byte[] sign(byte[] bytes) {

        SignResult signResult;

        try {
            final var messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(bytes);

            signResult = cryptographyClient.sign(SignatureAlgorithm.RS256, messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return signResult.getSignature();
    }

    @Override
    public String algorithm() {
        return "SHA256withRSA";
    }
}