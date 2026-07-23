package com.example.userservice.dto;

import com.example.userservice.entities.TipoUtenteEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UtenteRequest {

    private String nome;

    private String cognome;

    private String email;

    private String password;

    private String telefono;

    private TipoUtenteEnum tipoUtente;

    private int idSede;

}
