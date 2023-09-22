package com.junyounggoat.dreamstore.userservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Value("${spring.h2.console.enabled}")
    private boolean h2ConsoleEnabled;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector)
            throws Exception
    {
        if (h2ConsoleEnabled) {
            // 프로덕션에서는 사용 x, h2-console 디버깅 시에만 써야됨
            http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                authorizationManagerRequestMatcherRegistry.requestMatchers(toH2Console()).permitAll();
            });
            http.headers((httpSecurityHeadersConfigurer ->
                    httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)));
        }

        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
            MvcRequestMatcher.Builder builder = new MvcRequestMatcher.Builder(introspector);

            authorizationManagerRequestMatcherRegistry
                    .requestMatchers(builder.pattern(HttpMethod.GET, "/docs/index.html"),
                            builder.pattern(HttpMethod.GET, "/swagger-ui/*"),
                            builder.pattern(HttpMethod.GET, "/v3/api-docs"),
                            builder.pattern(HttpMethod.GET, "/v3/api-docs/*"),
                            builder.pattern(HttpMethod.GET, "/error"),
                            builder.pattern(HttpMethod.GET, "/"),
                            RegexRequestMatcher.regexMatcher(HttpMethod.GET, "^/api/v1/user/(\\d)+$"),
                            builder.pattern(HttpMethod.POST, "/api/v1/user"),
                            builder.pattern(HttpMethod.POST, "/api/v1/user/login"),
                            builder.pattern(HttpMethod.POST, "/api/v1/token/refresh")
                    ).permitAll()
                    .anyRequest().authenticated();
        });

        http.csrf((AbstractHttpConfigurer::disable))
                .httpBasic(AbstractHttpConfigurer::disable);

        http.sessionManagement(httpSecuritySessionManagementConfigurer -> {
            httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });

        http.addFilterAt(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
            httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(jwtAuthenticationEntryPoint);
            httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(jwtAccessDeniedHandler);
        });

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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
