package com.example.userservice.service;

import com.example.userservice.entities.Utente;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log
@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    private final UtenteService utenteService;
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(UtenteService utenteService, PasswordEncoder passwordEncoder) {
        this.utenteService = utenteService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utente utente = utenteService.getUtenteByEmail(email);

        if (utente == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        CustomUserDetails customUser = new CustomUserDetails(utente);
        log.info(customUser.getAuthorities().toString());
        return customUser;

    }

    public String addUser(Utente utente){
        utente.setPassword(passwordEncoder.encode(utente.getPassword()));
        utenteService.addUtente(utente);
        return "User added successfully";
    }
}
