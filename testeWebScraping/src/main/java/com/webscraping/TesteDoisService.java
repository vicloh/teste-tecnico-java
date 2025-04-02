package com.webscraping;

import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.*;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;

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
        substituicoes.put("OD", "Odontológico");
        substituicoes.put("AMB", "Ambulatorial");

        ObjectExtractor extractor = new ObjectExtractor(document);
        SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();

        PageIterator pageIterator = extractor.extract();
        List<Page> pages = new ArrayList<>();
        while (pageIterator.hasNext()) {
            pages.add(pageIterator.next());
        }
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvPath))) {
            for (Page page : pages) {
                List<Table> tables = sea.extract(page);
                for (Table table : tables) {
                    for (List<RectangularTextContainer> row : table.getRows()) {
                        String[] rowData = row.stream()
                                .map(RectangularTextContainer::getText)
                                .map(text -> substituicoes.getOrDefault(text, text)) // Substituição das siglas
                                .toArray(String[]::new);
                        writer.writeNext(rowData);
                    }
                }
            }
        }

        document.close();
    }
}
