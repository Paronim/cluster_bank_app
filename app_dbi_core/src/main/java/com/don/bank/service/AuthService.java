package com.don.bank.service;

import com.don.bank.config.security.CustomUserDetails;
import com.don.bank.dto.LoginClientDTO;
import com.don.bank.dto.RegisterClientDTO;
import com.don.bank.entity.Client;
import com.don.bank.util.JWT.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final ClientService clientService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthService(ClientService clientService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.clientService = clientService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public void register (RegisterClientDTO registerClientDTO){

        boolean valid = clientService.existsByPhone(Long.parseLong(registerClientDTO.getPhone()));

        if(!valid){
            throw new IllegalArgumentException("Client with phone number " + registerClientDTO.getPhone() + " already exists");
        }

        registerClientDTO.setPassword(passwordEncoder.encode(registerClientDTO.getPassword()));

        clientService.addClient(registerClientDTO);
    }

    public Map<String, Object> login (LoginClientDTO loginClientDTO){

        Client client = clientService.getByPhone(Long.parseLong(loginClientDTO.getPhone())).orElseGet(null);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(client.getId(), loginClientDTO.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userData = (CustomUserDetails) authentication.getPrincipal();

        return Map.of("token", jwtUtils.generateToken(userData.getUsername()), "id", client.getId());
    }
}
