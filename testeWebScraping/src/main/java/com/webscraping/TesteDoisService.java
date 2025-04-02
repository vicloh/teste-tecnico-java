package com.webscraping;

import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.*;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;
import java.nio.charset.StandardCharsets;
import org.apache.commons.text.StringEscapeUtils;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;
import java.io.*;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class TesteDoisService {
    public void extractTableToCSV(String pdfPath, String csvPath) throws IOException {
        File pdfFile = new File(pdfPath);
        if (!pdfFile.exists()) {
            throw new FileNotFoundException("Arquivo PDF não encontrado: " + pdfPath);
        }

        PDDocument document = PDDocument.load(pdfFile);

        // Criando o mapa de substituições
        Map<String, String> substituicoes = new HashMap<>();
        substituicoes.put("OD", "Odontologico");
        substituicoes.put("AMB", "Ambulatorial");

        ObjectExtractor extractor = new ObjectExtractor(document);
        SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();

        PageIterator pageIterator = extractor.extract();
        List<Page> pages = new ArrayList<>();
        while (pageIterator.hasNext()) {
            pages.add(pageIterator.next());
        }

        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(csvPath), StandardCharsets.UTF_8))) {
            for (Page page : pages) {
                List<Table> tables = sea.extract(page);
                for (Table table : tables) {
                    for (List<RectangularTextContainer> row : table.getRows()) {
                        String[] rowData = row.stream()
                                .map(RectangularTextContainer::getText)
                                .map(TesteDoisService::removerAcentos) // Remove acentos e caracteres especiais
                                .map(StringEscapeUtils::unescapeHtml4) // Corrige caracteres HTML
                                .map(text -> substituicoes.getOrDefault(text, text)) // Substituições de siglas
                                .toArray(String[]::new);
                        writer.writeNext(rowData);
                    }
                }
            }
        }

        document.close();
    }

    public static String removerAcentos(String texto) {
        if (texto == null) return "";
        // Normaliza para decompor acentos e diacríticos
        String textoNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        // Remove os caracteres diacríticos (acentos)
        textoNormalizado = textoNormalizado.replaceAll("\\p{M}", "");
        // Remove caracteres especiais como % e mantém apenas letras, números, espaços e pontuação básica
        textoNormalizado = textoNormalizado.replaceAll("[^\\p{L}\\p{N}\\p{P}\\p{Z}]", "");
        return textoNormalizado;
    }
}
