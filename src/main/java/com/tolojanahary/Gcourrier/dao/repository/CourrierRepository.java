package com.tolojanahary.Gcourrier.dao.repository;

import com.tolojanahary.Gcourrier.dao.entity.Courrier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourrierRepository extends JpaRepository<Courrier, Long> {
}
