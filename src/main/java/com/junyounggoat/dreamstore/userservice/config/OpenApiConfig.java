package com.junyounggoat.dreamstore.userservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.junyounggoat.dreamstore.userservice.config.JwtFilter.AUTHORIZATION_HEADER;

@Configuration
public class OpenApiConfig {
    public static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    @Bean
    public OpenAPI openAPI(@Value("v1.0") String appVersion) {
        Components components = new Components().addSecuritySchemes(
                SECURITY_SCHEME_NAME,
                new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .bearerFormat("JWT")
                        .scheme("bearer"));

        Info info = new Info().title("준영GOAT의 꿈백화점 - UserService").version(appVersion)
                .description("UserService API 문서입니다.")
                .contact(new Contact().name("nab5m")
                        .url("https://github.com/nab5m/dream-store-user-service")
                        .email("disnwkdl420@gmail.com"));

        return new OpenAPI()
                .components(components)
                .info(info);
    }
}
