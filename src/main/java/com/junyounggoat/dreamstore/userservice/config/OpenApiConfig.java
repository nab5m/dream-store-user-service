package com.junyounggoat.dreamstore.userservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI(@Value("v1.0") String appVersion) {
        Info info = new Info().title("준영GOAT의 꿈백화점 - UserService").version(appVersion)
                .description("UserService API 문서입니다.")
                .contact(new Contact().name("nab5m")
                        .url("https://github.com/nab5m/dream-store-user-service")
                        .email("disnwkdl420@gmail.com"));

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
