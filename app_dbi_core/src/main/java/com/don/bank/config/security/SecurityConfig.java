package com.don.bank.config.security;

import com.don.bank.dto.LoginClientDTO;
import com.don.bank.dto.RegisterClientDTO;
import com.don.bank.entity.Client;
import com.don.bank.service.AuthService;
import com.don.bank.service.ClientService;
import com.don.bank.util.JWT.JWTTokenCookie;
import com.don.bank.util.JWT.JwtAuthenticationFilter;
import com.don.bank.util.JWT.JwtUtils;
import com.don.bank.util.passwordGeneratorUtils.PasswordGenerator;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;
import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    private final JwtUtils jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final ClientService clientService;
    private final AuthService authService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtUtils jwtUtil, ClientService clientService, @Lazy AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
        this.clientService = clientService;
        this.authService = authService;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
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
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .failureHandler((request, response, exception) -> {
                            log.error("Error OAuth2: {}", exception.getMessage(), exception);
                            response.sendRedirect("/login?error");
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
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, customUserDetailsService), UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> {
                    logout.logoutUrl("/auth/logout");
                    logout.logoutSuccessHandler((request, response, authentication) -> {

                        JWTTokenCookie.removeToken(response);

                        SecurityContextHolder.clearContext();

                        response.sendRedirect("/login");
                    });
                });

        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
        return userRequest -> {
            OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);

            Map<String, Object> phoneData = (Map<String, Object>) oauth2User.getAttributes().get("default_phone");
            String firstName = (String) oauth2User.getAttributes().get("first_name");
            String lastName = (String) oauth2User.getAttributes().get("last_name");
            String phoneNumber;

            if (phoneData != null) {
                phoneNumber = (String) phoneData.get("number");
            } else {
                log.error("phone is null");
                throw new RuntimeException("phone is null");
            }


            if(clientService.existsByPhone(Long.parseLong(phoneNumber))) {
                String password = PasswordGenerator.generatePassword(12);

                authService.register(RegisterClientDTO.builder()
                                .firstName(firstName)
                                .lastName(lastName)
                                .password(password)
                                .phone(phoneNumber.substring(1))
                                .build());
            };

            Optional<Client> clientData = clientService.getByPhone(Long.parseLong(phoneNumber));
            Client client = clientData.get();

            String token = jwtUtil.generateToken(String.valueOf(client.getId()));

            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            JWTTokenCookie.addToken(response, token);

            return oauth2User;
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
