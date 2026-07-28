package com.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//modello per lista utenti
public class UtenteDTO {

    private String nome;

    private String cognome;

    private String email;

    private String password;

    private String telefono;

    private String tipoUtente;

    private String userKey;

}
