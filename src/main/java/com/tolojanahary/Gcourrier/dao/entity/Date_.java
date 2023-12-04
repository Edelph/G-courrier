package com.tolojanahary.Gcourrier.dao.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
public class Date_ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long ID_date;
    LocalDate date_envoie;
    LocalDate date_recep;
}
