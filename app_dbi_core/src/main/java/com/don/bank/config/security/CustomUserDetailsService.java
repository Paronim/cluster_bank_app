package com.don.bank.config.security;

import com.don.bank.controller.AuthController;
import com.don.bank.entity.Client;
import com.don.bank.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final ClientRepository clientRepository;

    public CustomUserDetailsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {

        Optional<Client> client = clientRepository.findClientByPhone(Long.parseLong(phone));

        return client.map(CustomUserDetails::new).orElseThrow(() -> {
            log.warn("No client found with phone: {}", phone);
            return new UsernameNotFoundException(phone + " There is no such client");
        });

    }
}
