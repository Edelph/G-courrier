package com.tolojanahary.Gcourrier.dao.repository;

import com.tolojanahary.Gcourrier.dao.entity.Expediteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpediteurRepository extends JpaRepository<Expediteur, Long> {
    List<Expediteur> findByEmailContains(String email);
}
