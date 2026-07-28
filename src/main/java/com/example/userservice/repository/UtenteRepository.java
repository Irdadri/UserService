package com.example.userservice.repository;

import com.example.userservice.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UtenteRepository extends JpaRepository<Utente, Integer > {
    public Utente findUtenteByEmail(String email);
    public Utente findUtenteById(int id);

    public Utente findUtenteByUserKey(String userKey);
}
