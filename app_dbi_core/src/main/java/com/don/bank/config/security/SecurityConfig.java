package com.don.bank.config.security;

import com.don.bank.util.JWT.JwtAuthenticationFilter;
import com.don.bank.util.JWT.JwtUtils;
import com.don.bank.util.OAuth2Utils.CustomOAuth2LoginAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    private final JwtUtils jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtUtils jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return customUserDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   ClientRegistrationRepository clientRegistrationRepository,
                                                   OAuth2AuthorizedClientService authorizedClientService) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable
                )
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/auth/register",
                                "/auth/login",
                                "/login",
                                "/register",
                                "/error",
                                "/login/oauth2/code/yandex",
                                "/WEB-INF/views/login.jsp",
                                "/WEB-INF/views/error.jsp",
                                "/WEB-INF/views/register.jsp",
                                "/WEB-INF/views/yandexAuth.jsp",
                                "/favicon.ico",
                                "/login?error",
                                "/login/yandex",
                                "/js/**",
                                "/css/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .failureHandler((request, response, exception) -> {
                            log.error(request.getRequestURI());
                            log.error(request.getQueryString());
                            log.error("Error OAuth2: {}", exception.getMessage(), exception);
//                            response.sendRedirect("/login");
                        })
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService())
                        )
                )
                .exceptionHandling(e -> e.authenticationEntryPoint((request, response, authException) -> {

                    log.info(request.getRequestURI());
                    log.error(authException.getMessage(), authException);

                    response.sendRedirect("/login");
                }))
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
                .addFilterBefore(
                        new CustomOAuth2LoginAuthenticationFilter(),
                        OAuth2LoginAuthenticationFilter.class
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, customUserDetailsService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
        return userRequest -> {
            System.out.println("test");
            OAuth2AccessToken accessToken = userRequest.getAccessToken();
            OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);

            System.out.println(oauth2User.getAttributes().toString());

            return oauth2User;
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
