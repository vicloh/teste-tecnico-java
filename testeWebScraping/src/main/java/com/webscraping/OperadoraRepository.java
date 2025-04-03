package com.webscraping.repository;

import com.webscraping.model.Operadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperadoraRepository extends JpaRepository<Operadora, Long> {
    List<Operadora> findByNomeFantasiaContainingIgnoreCase(String nomeFantasia);
}
