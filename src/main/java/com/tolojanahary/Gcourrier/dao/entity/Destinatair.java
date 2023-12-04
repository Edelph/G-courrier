package com.tolojanahary.Gcourrier.dao.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
public class Destinatair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id_destinataire;
    String nom_dest;
    String prenom_dest;
    String adresse_dest;
    String email_dest;
    String telephone_dest;
}
