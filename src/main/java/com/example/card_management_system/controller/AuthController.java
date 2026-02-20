package com.example.card_management_system.controller;

import com.example.card_management_system.record.AuthRequest;
import com.example.card_management_system.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtService jwtService;

    @Value("${app.auth.client-id}")
    private String trustedClientId;

    @Value("${app.auth.client-secret}")
    private String trustedClientSecret;

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> getToken(@RequestBody AuthRequest request) {
        log.info("Token request received for client: {}", request.clientId());

        if (request.clientId().equals(trustedClientId) && request.clientSecret().equals(trustedClientSecret)) {
            log.info("Authentication successful for client: {}", request.clientId());

            String token = jwtService.generateToken(request.clientId());
            return ResponseEntity.ok(Map.of("access_token", token));
        }

        log.warn("Authentication failed for client: {}", request.clientId());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
