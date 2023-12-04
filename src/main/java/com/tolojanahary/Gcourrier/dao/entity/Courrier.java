package com.tolojanahary.Gcourrier.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
public class Courrier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long ID_courrier;
    String type;
    String objet;
    String contenu;
    String statut;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_date")
    Date_ date;
    @ManyToOne
    @JoinColumn(name = "id_expediteur")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Expediteur expediteur;
    @ManyToOne
    @JoinColumn(name = "id_destinataire")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Destinatair destinatair;
}
