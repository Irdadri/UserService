package com.example.userservice.service;

import com.example.userservice.dto.UtenteDTO;
import com.example.userservice.dto.UtenteRequest;
import com.example.userservice.entities.Utente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


public interface UtenteService {
    public Utente getUtenteByEmail(String email);
    public Utente addUtente(Utente utente);
    public String creaUtente(UtenteRequest utenteRequest);
    public String updateUtente(UtenteRequest utente, String unique);
    public Page<UtenteDTO> getAllUtenti(Pageable pageable);
    public UtenteDTO getCurrentUtente(String unique);
    public void deleteUser(String userKey);
}
