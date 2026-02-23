package com.example.card_management_system.controller;

import com.example.card_management_system.record.AuthRequest;
import com.example.card_management_system.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
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
    @Operation(security = @SecurityRequirement(name = "basicAuth"))
    public ResponseEntity<?> getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || authHeader.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing auth header");
        }

        String base64credentials = authHeader.substring(6);
        byte[] decodedBytes = Base64.getDecoder().decode(base64credentials);
        String credentials = new String(decodedBytes);

        String[] values = credentials.split(":", 2);
        String clientId = values[0];
        String clientSecret = values[1];

        log.info("Token request received for client: {}", clientId);

        if (clientId.equals(trustedClientId) && clientSecret.equals(trustedClientSecret)) {
            log.info("Authentication successful for client: {}", clientId);

            String token = jwtService.generateToken(clientId);
            return ResponseEntity.ok(Map.of("access_token", token));
        }

        log.warn("Authentication failed for client: {}", clientId);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
