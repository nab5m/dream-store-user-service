package com.junyounggoat.dreamstore.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector)
            throws Exception
    {
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
            MvcRequestMatcher.Builder builder = new MvcRequestMatcher.Builder(introspector);

            authorizationManagerRequestMatcherRegistry
                    .requestMatchers(builder.pattern(HttpMethod.GET, "/docs/index.html")).permitAll()
                    .requestMatchers(builder.pattern(HttpMethod.GET, "/swagger-ui/*")).permitAll()
                    .requestMatchers(builder.pattern(HttpMethod.GET, "/v3/api-docs")).permitAll()
                    .requestMatchers(builder.pattern(HttpMethod.GET, "/v3/api-docs/*")).permitAll()
                    .requestMatchers(builder.pattern(HttpMethod.GET, "/error")).permitAll()
                    .requestMatchers(builder.pattern(HttpMethod.GET, "/")).permitAll()
                    .anyRequest().authenticated();
        });

        return http.build();
    }
}
