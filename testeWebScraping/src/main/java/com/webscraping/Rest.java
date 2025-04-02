package com.webscraping;

import com.fasterxml.jackson.annotation.JacksonInject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api")
public class Rest {

    private final TesteUmService testeUm;
    private final TesteDoisService testeDois;

    @Autowired
    public Rest(TesteUmService testeUm, TesteDoisService testeDois) {
        this.testeUm = testeUm;
        this.testeDois = testeDois;
    }

    @GetMapping("/zip")
    public ResponseEntity<Resource> zipPdfs() {
        try {
            String pdfUrl1= "https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos/Anexo_I_Rol_2021RN_465.2021_RN627L.2024.pdf";
            String pdfUrl2= "https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos/Anexo_II_DUT_2021_RN_465.2021_RN628.2025_RN629.2025.pdf";
            String file1 = "Anexo_I.pdf";
            String file2 = "Anexo_II.pdf";
            String zipFileName = "output.zip";

            // Baixar os PDFs
            testeUm.downloadFile(pdfUrl1, file1);
            testeUm.downloadFile(pdfUrl2, file2);

            // Criar o ZIP
            testeUm.zipFiles(zipFileName, file1, file2);

            // Retornar o ZIP como resposta
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(Paths.get(zipFileName)));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=output.zip")
                    .body(resource);
        } catch (Exception e) {
            System.err.println("Erro durante o processamento:");
            e.printStackTrace(); // Exibir erro completo no console
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/tabelaCSV")
    public ResponseEntity<Resource> extractCsv() {

        if (testeDois == null) {
            System.err.println("testeDoisService não foi injetado corretamente!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        try {

            System.out.println("testeDois: " + testeDois); // Verifica se a injeção ocorreu

            String pdfPath = "Anexo_I.pdf"; // Precisa já estar baixado
            String csvPath = "Rol_Procedimentos.csv";

            testeDois.extractTableToCSV(pdfPath, csvPath);

            // Retornar o CSV como resposta
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(Paths.get(csvPath)));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Rol_Procedimentos.csv")
                    .body(resource);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            System.err.println("Erro durante o processamento:");
            e.printStackTrace(); // Exibir erro completo no console
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}



