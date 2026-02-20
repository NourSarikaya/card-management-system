package com.example.card_management_system.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI customOpenAPI() {

        String securitySchemeName = "JWT_Authentication_Gate";

        return new OpenAPI().info(new Info().title("Card Management System API")
                                            .version("1.0")
                                            .description("M2M Authenticated API for "))
                            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                            .components(new Components()
                                    .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                            .name(securitySchemeName)
                                            .type(SecurityScheme.Type.HTTP)
                                            .scheme("bearer")
                                            .bearerFormat("JWT")));

    }
}
