package com.example.userservice.service;

import com.example.userservice.dto.UtenteRequest;
import com.example.userservice.entities.Utente;
import org.springframework.stereotype.Service;


public interface UtenteService {
    public Utente getUtenteByEmail(String email);
    public Utente addUtente(Utente utente);
    public Utente aggiornaOAggiungi(UtenteRequest utente, int idUser);

    public void deleteUser(int id);
}
