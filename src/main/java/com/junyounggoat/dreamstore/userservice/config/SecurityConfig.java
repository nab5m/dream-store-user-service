package com.junyounggoat.dreamstore.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

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
                    .requestMatchers(builder.pattern(HttpMethod.GET, "/docs/index.html"),
                            builder.pattern(HttpMethod.GET, "/swagger-ui/*"),
                            builder.pattern(HttpMethod.GET, "/v3/api-docs"),
                            builder.pattern(HttpMethod.GET, "/v3/api-docs/*"),
                            builder.pattern(HttpMethod.GET, "/error"),
                            toH2Console(),
                            builder.pattern(HttpMethod.GET, "/")
                    ).permitAll()
                    .anyRequest().authenticated();
        });

        http.csrf((AbstractHttpConfigurer::disable))
                .httpBasic(AbstractHttpConfigurer::disable);

        // ToDo: iframe 허용 : h2-console 디버깅 시에만 써야됨
        // 프로필 정보를 토대로 개발 환경에서만 적용할 수 있을까?
        http.headers((httpSecurityHeadersConfigurer ->
                httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)));

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(HandlerMappingIntrospector introspector) {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        return web -> {
            web.ignoring().requestMatchers(
                    mvcMatcherBuilder.pattern("/error")
            );
        };
    }
}
