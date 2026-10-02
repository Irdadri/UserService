package com.example.userservice.service;

import com.example.userservice.dto.UtenteDTO;
import com.example.userservice.dto.UtenteRequest;
import com.example.userservice.entities.TipoUtenteEnum;
import com.example.userservice.entities.Utente;
import com.example.userservice.repository.UtenteRepository;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
    @Cacheable(value = "utenteEmail", key = "#email")
    public Utente getUtenteByEmail(String email) {
        return repository.findUtenteByEmail(email);
    }


    @Override
    public Utente addUtente(Utente utente) {
        return repository.save(utente);
    }

    @Override
    @Caching( evict = {
            @CacheEvict(value = "utente", allEntries = true),
            @CacheEvict(value="utente_sede", allEntries = true),
            @CacheEvict(value = "allUser", allEntries = true)
    })
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
    @Caching( evict = {
            @CacheEvict(value = "utente", allEntries = true),
            @CacheEvict(value="utente_sede", allEntries = true),
            @CacheEvict(value = "allUser", allEntries = true)
    })
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
    @Cacheable(value = "allUtenti", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<UtenteDTO> getAllUtenti(Pageable pageable) {
        return repository.findAll(pageable).map(utente -> modelMapper.map(utente, UtenteDTO.class));
    }

    @Override
    @Cacheable(value = "utente", key = "#userKey")
    public UtenteDTO getCurrentUtente(String userKey) {
        log.info(userKey);
        Utente utente = repository.findUtenteByUserKey(userKey);
        if (utente == null) {
            return null;
        }
        return modelMapper.map(utente, UtenteDTO.class);
    }


    @Override
    @Caching( evict = {
            @CacheEvict(value = "utente", allEntries = true),
            @CacheEvict(value="utente_sede", allEntries = true),
            @CacheEvict(value = "allUser", allEntries = true)
    })
    public void deleteUser(String userKey) {
        Utente utente = repository.findUtenteByUserKey(userKey);
        if (utente == null) {
            throw new UsernameNotFoundException("utente non trovato");
        }
        repository.delete(utente);
    }


}
