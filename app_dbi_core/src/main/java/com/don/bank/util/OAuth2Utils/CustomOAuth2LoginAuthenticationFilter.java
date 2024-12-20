package com.don.bank.util.OAuth2Utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;

public class CustomOAuth2LoginAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2LoginAuthenticationFilter.class);
    private final AuthenticationManager authenticationManager;

    public CustomOAuth2LoginAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getParameter("access_token");

        if (token != null) {
            OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, token, Instant.now(), Instant.now().plusSeconds(3600));

            OAuth2LoginAuthenticationToken authenticationToken = new OAuth2LoginAuthenticationToken(null, null);
            authenticationToken.setDetails(authenticationToken);

            authenticationManager.authenticate(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }
}

