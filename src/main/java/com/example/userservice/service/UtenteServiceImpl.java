package com.example.userservice.service;

import com.example.userservice.dto.UtenteRequest;
import com.example.userservice.entities.TipoUtenteEnum;
import com.example.userservice.entities.Utente;
import com.example.userservice.repository.UtenteRepository;
import lombok.extern.java.Log;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

@Service
@Log
public class UtenteServiceImpl implements UtenteService {

    private final UtenteRepository repository;
    private final PasswordEncoder passwordEncoder;


    public UtenteServiceImpl(UtenteRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Utente getUtenteByEmail(String email) {
        return repository.findUtenteByEmail(email);
    }


    @Override
    public Utente addUtente(Utente utente) {
        return repository.save(utente);
    }

    @Override
    public Utente aggiornaOAggiungi(UtenteRequest utente, int idUser) {
        Utente _utente = repository.findUtenteByEmail(utente.getEmail());
        if(_utente != null){
            _utente.setId(idUser);
            _utente.setNome(utente.getNome());
            _utente.setCognome(utente.getCognome());
            _utente.setPassword(passwordEncoder.encode(utente.getPassword()));
            _utente.setTipoUtente(utente.getTipoUtente());
            _utente.setTelefono(utente.getTelefono());

            return repository.save(_utente);
        } else {
            Utente newUtente = new Utente();
            newUtente.setId(idUser);
            newUtente.setNome(utente.getNome());
            newUtente.setCognome(utente.getCognome());
            newUtente.setEmail(utente.getEmail());
            newUtente.setPassword(passwordEncoder.encode(utente.getPassword()));
            newUtente.setTelefono(utente.getTelefono());
            newUtente.setTipoUtente(utente.getTipoUtente());
            return repository.save(newUtente);
        }
    }

    @Override
    public void deleteUser(int id) {
        Utente utente = repository.findUtenteById(id);
        repository.delete(utente);
    }


}
