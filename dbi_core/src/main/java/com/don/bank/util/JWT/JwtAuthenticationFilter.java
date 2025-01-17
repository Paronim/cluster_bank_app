package com.don.bank.util.JWT;

import com.don.bank.config.security.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtil;

    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtils jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String authorizationHeader = Arrays.stream(request.getCookies())
                    .filter(cookie -> "token".equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);

            String id = null;
            String jwt = null;

            if (authorizationHeader != null) {
                jwt = authorizationHeader;
                id = jwtUtil.extractUsername(jwt);
            }

            if (id != null) {

                UserDetails userDetails;

                try {
                    userDetails = userDetailsService.loadUserByUsername(id);
                } catch (UsernameNotFoundException e) {
                    JWTTokenCookie.removeToken(response);
                    SecurityContextHolder.clearContext();
                    response.sendRedirect("/login");
                    return;
                }

                if (jwtUtil.validateToken(jwt, userDetails.getUsername()) && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            JWTTokenCookie.removeToken(response);

            response.sendRedirect("/login");
        }

    }
}
