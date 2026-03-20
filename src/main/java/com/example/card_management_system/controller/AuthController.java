package com.example.card_management_system.controller;

import com.example.card_management_system.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Profile("!test")
public class AuthController {
    private final JwtService jwtService;

    @PostMapping("/token")
    @Operation(security = @SecurityRequirement(name = "basicAuth"))
    public ResponseEntity<?> getToken(Authentication authentication) {
        String token = jwtService.generateToken(authentication);

        return ResponseEntity.ok(Map.of(
                "access_token", token,
                "token_type", "Bearer",
                "expires_in", 36000
        ));
    }
}
