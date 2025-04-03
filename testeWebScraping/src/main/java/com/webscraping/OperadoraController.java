package com.webscraping.controller;

import com.webscraping.model.Operadora;
import com.webscraping.service.OperadoraService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/operadoras")
public class OperadoraController {

    private final OperadoraService operadoraService;

    public OperadoraController(OperadoraService operadoraService) {
        this.operadoraService = operadoraService;
    }

    @GetMapping("/buscar")
    public List<Operadora> buscarOperadoras(@RequestParam String nome) {
        return operadoraService.buscarPorNome(nome);
    }
}
