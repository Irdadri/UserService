package com.example.userservice.service;

import com.example.userservice.dto.UtenteDTO;
import com.example.userservice.dto.UtenteRequest;
import com.example.userservice.entities.TipoUtenteEnum;
import com.example.userservice.entities.Utente;
import com.example.userservice.repository.UtenteRepository;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log
public class UtenteServiceImpl implements UtenteService {

    private final UtenteRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;


    public UtenteServiceImpl(UtenteRepository repository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
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
    public String creaUtente(UtenteRequest utenteRequest) {

        Utente findUtente = repository.findUtenteByEmail(utenteRequest.getEmail());
        if(findUtente == null) {
            //se non esiste, inserisci
            Utente utente = modelMapper.map(utenteRequest, Utente.class);
            utente.setUserKey(UUID.randomUUID().toString());
            utente.setPassword(passwordEncoder.encode(utente.getPassword()));
            repository.save(utente);
            return utente.getUserKey();
        } else {
            //se esiste già non fare nulla
            //o dovrei lanciare un errore
            return null;
        }
    }

    @Override
    public String updateUtente(UtenteRequest utente, String userKey) {
        Utente _utente = repository.findUtenteByUserKey(userKey);

        if (_utente != null) {


            _utente.setNome(utente.getNome());
            _utente.setCognome(utente.getCognome());
            _utente.setPassword(passwordEncoder.encode(utente.getPassword()));
            _utente.setTipoUtente(utente.getTipoUtente());
            _utente.setTelefono(utente.getTelefono());

            repository.save(_utente);
            return _utente.getUserKey();

        } else {
            throw new UsernameNotFoundException("utente non esistente");
        }
    }

    @Override
    public Page<UtenteDTO> getAllUtenti(Pageable pageable) {
        return repository.findAll(pageable).map(utente -> modelMapper.map(utente, UtenteDTO.class));
    }

    @Override
    public UtenteDTO getCurrentUtente(String userKey) {
        log.info(userKey);
        Utente utente = repository.findUtenteByUserKey(userKey);
        if (utente == null) {
            return null;
        }
        return modelMapper.map(utente, UtenteDTO.class);
    }


    @Override
    public void deleteUser(String userKey) {
        Utente utente = repository.findUtenteByUserKey(userKey);
        if (utente == null) {
            throw new UsernameNotFoundException("utente non trovato");
        }
        repository.delete(utente);
    }


}
