package com.don.bank.config.security;

import com.don.bank.entity.Client;
import com.don.bank.service.ClientService;
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

    private final ClientService clientService;

    public CustomUserDetailsService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        Optional<Client> client = clientService.getEntityById(Long.parseLong(id));

        return client.map(CustomUserDetails::new).orElseThrow(() -> {
            log.warn("No client found with id: {}", id);
            return new UsernameNotFoundException(id + " There is no such client");
        });

    }
}
