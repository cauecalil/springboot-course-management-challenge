package org.cauecalil.coursemanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info().title("Course Management API").version("1.0"))
                .schemaRequirement("jwt_auth", createSecurityScheme())
        ;
    }

    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .name("jwt_auth")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        ;
    }
}
