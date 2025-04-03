package com.webscraping.service;

import com.webscraping.model.Operadora;
import com.webscraping.repository.OperadoraRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OperadoraService {

    private final OperadoraRepository operadoraRepository;

    public OperadoraService(OperadoraRepository operadoraRepository) {
        this.operadoraRepository = operadoraRepository;
    }

    public List<Operadora> buscarOperadorasPorNome(String nomeFantasia) {
        return operadoraRepository.findByNomeFantasiaContainingIgnoreCase(nomeFantasia);
    }
}
