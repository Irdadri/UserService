package com.example.userservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "utente")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Utente {
    @Id
    private int id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cognome")
    private String cognome;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "telefono")
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_utente")
    private TipoUtenteEnum tipoUtente;

    @Column(name = "uuid")
    private String userKey;
}
