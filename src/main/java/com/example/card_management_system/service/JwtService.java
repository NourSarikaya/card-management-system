package com.example.card_management_system.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

//    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
//    private String issuer;

    public String generateToken(String clientId) {
        try {
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder().subject(clientId)
                                                               .issuer("card-management-system")
                                                               .issueTime(new Date())
                                                               .expirationTime(Date.from(Instant.now().plusSeconds(3600)))
                                                               .build();

            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
            SignedJWT signedJWT = new SignedJWT(header, claimsSet);

            JWSSigner signer = new MACSigner(secret.getBytes());
            signedJWT.sign(signer);

            return signedJWT.serialize();

        } catch (JOSEException e) {
            throw new RuntimeException("Error generation JWT", e);
        }
    }

}
