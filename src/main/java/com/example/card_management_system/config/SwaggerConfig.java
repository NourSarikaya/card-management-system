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

        String bearerAuth = "bearerAuth";
        String basicAuth = "basicAuth";

        return new OpenAPI().info(new Info().title("Card Management System API")
                                            .version("1.0")
                                            .description("Secure API for card management system"))
                            .components(new Components()
                                    .addSecuritySchemes(bearerAuth, new SecurityScheme()
                                            .name(bearerAuth)
                                            .type(SecurityScheme.Type.HTTP)
                                            .scheme("bearer")
                                            .bearerFormat("JWT"))
                                    .addSecuritySchemes(basicAuth, new SecurityScheme()
                                            .name(basicAuth)
                                            .type(SecurityScheme.Type.HTTP)
                                            .scheme("basic")));
//                            .addSecurityItem(new SecurityRequirement()
//                                    .addList(bearerAuth)
//                                    .addList(basicAuth));

    }
}
