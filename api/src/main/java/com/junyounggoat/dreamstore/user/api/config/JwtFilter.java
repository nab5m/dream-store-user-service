package com.junyounggoat.dreamstore.user.api.config;

import com.junyounggoat.dreamstore.user.api.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.RequiredTypeException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class JwtFilter extends GenericFilterBean {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // ToDo: 에외처리를 더 깔끔하게 할 수 없을까?
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = resolveToken(httpServletRequest);
        if (token == null) {
            chain.doFilter(request, response);
            return;
        }

        Claims claims = TokenService.getClaims(token);
        if (claims == null) {
            chain.doFilter(request, response);
            return;
        }

        UserDetails userDetails;
        try {
             userDetails = User.builder()
                    .username(claims.get(TokenService.JWT_CLAIM_USER_ID, Long.class).toString())
                    .password(token)
                    .build();
        } catch (RequiredTypeException e) {
            logger.info("TypeCastUserId Failed : " + token);

            chain.doFilter(request, response);
            return;
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(AUTHORIZATION_HEADER);

        if (token != null && !token.isBlank() && token.startsWith(AUTHORIZATION_HEADER_PREFIX)) {
            return token.substring(AUTHORIZATION_HEADER_PREFIX.length());
        }

        return null;
    }
}
