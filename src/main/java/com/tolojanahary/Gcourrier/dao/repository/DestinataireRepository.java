package com.tolojanahary.Gcourrier.dao.repository;

import com.tolojanahary.Gcourrier.dao.entity.Destinatair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinataireRepository extends JpaRepository<Destinatair, Long> {
    @Query("SELECT d FROM Destinatair d WHERE d.email_dest LIKE %?1%")
    List<Destinatair> findByEmail_destContains(String email);
}
