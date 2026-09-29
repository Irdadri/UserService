package com.example.userservice.controller;


import com.example.userservice.dto.JwtTokenResponse;
import com.example.userservice.dto.LoginRequest;
import com.example.userservice.dto.UtenteDTO;
import com.example.userservice.dto.UtenteRequest;
import com.example.userservice.entities.Utente;
import com.example.userservice.security.JwtService;
import com.example.userservice.service.BasicUserDetails;
import com.example.userservice.service.UtenteService;

import jakarta.validation.Valid;
import lombok.extern.java.Log;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final ModelMapper modelMapper;


    public AuthController(
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            UtenteService utenteService, ModelMapper modelMapper) {

        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.utenteService = utenteService;

        this.modelMapper = modelMapper;
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
    public ResponseEntity<UtenteDTO> getUtente(@PathVariable("email") String email) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        log.info("Richiesta effettuata da: "
                + authentication.getName());

        Utente utente = utenteService.getUtenteByEmail(email);
        UtenteDTO utenteDTO;
        if(utente != null){
            utenteDTO = modelMapper.map(utente, UtenteDTO.class);
            log.info(utenteDTO.getPassword());
        } else {
            utenteDTO = null;
        }

        return ResponseEntity.ok(utenteDTO);
    }

    @GetMapping("/getUtenti")
    public ResponseEntity<Page<UtenteDTO>> getAllUtenti(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(utenteService.getAllUtenti(pageable));
    }

    @PostMapping("/currentUtente")
    public ResponseEntity<?> getCurrentUtente(@RequestBody String userKey) {
        UtenteDTO utenteDTO =utenteService.getCurrentUtente(userKey);

        if(utenteDTO != null){
            log.info(utenteDTO.getUserKey());
            return ResponseEntity.ok(utenteService.getCurrentUtente(userKey));

        } else {
            return ResponseEntity.notFound().build();
        }

    }


    @PostMapping(value = "/creaUtente")
    public ResponseEntity<?> creaUtente(@Valid @RequestBody UtenteRequest utente,
                                        BindingResult bindingResult) {
        return ResponseEntity.ok(utenteService.creaUtente(utente));
    }

    @PostMapping(value = "/updateUtente/{userKey}")
    public ResponseEntity<?> aggiornaUtente(@Valid @RequestBody UtenteRequest utente,
                                            BindingResult bindingResult,
                                            @PathVariable String userKey) {
        try {
            return ResponseEntity.ok(utenteService.updateUtente(utente, userKey));
        } catch (UsernameNotFoundException e){
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping(value = "/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestParam String userKey) {
        try {
            utenteService.deleteUser(userKey);
        } catch (UsernameNotFoundException e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

}
