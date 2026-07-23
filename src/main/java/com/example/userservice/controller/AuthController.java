package com.example.userservice.controller;


import com.example.userservice.dto.JwtTokenResponse;
import com.example.userservice.dto.LoginRequest;
import com.example.userservice.dto.UtenteRequest;
import com.example.userservice.entities.Utente;
import com.example.userservice.security.JwtService;
import com.example.userservice.service.BasicUserDetails;
import com.example.userservice.service.UtenteService;

import jakarta.validation.Valid;
import lombok.extern.java.Log;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@Log
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UtenteService utenteService;



    public AuthController(
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            UtenteService utenteService) {

        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.utenteService = utenteService;

    }

    @PostMapping("/generateToken")
    public ResponseEntity<JwtTokenResponse> authenticateAndGetToken(@RequestBody LoginRequest authRequest) {
        log.info(authRequest.getEmail() + " " + authRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return ResponseEntity.ok(new JwtTokenResponse(jwtService.generateToken(authRequest.getEmail())));
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @GetMapping(value = "/cerca/{email}")
    public Utente getUtente(@PathVariable("email") String email) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        log.info("Richiesta effettuata da: "
                + authentication.getName());

        return utenteService.getUtenteByEmail(email);
    }

    @PostMapping(value="/creaUtente/{idUser}")
    public ResponseEntity<?> creaOAggiornaUtente(@Valid @RequestBody UtenteRequest utente,
                                                 BindingResult bindingResult,
                                                 @PathVariable int idUser){
        Utente newUtente = utenteService.aggiornaOAggiungi(utente, idUser);

        if(utente != null){
            return ResponseEntity.ok(utente);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value="/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id){
        utenteService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

}
