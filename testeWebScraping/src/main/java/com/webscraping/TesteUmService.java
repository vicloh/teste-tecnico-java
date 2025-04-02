package com.webscraping;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class TesteUmService {
    public void downloadFile(String fileURL, String fileName) throws IOException {
        try (BufferedInputStream in = new BufferedInputStream(new URL(fileURL).openStream());
             FileOutputStream fos = new FileOutputStream(fileName)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
    }

    public void zipFiles(String zipFileName, String... files) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFileName);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (String file : files) {
                File f = new File(file);
                if (!f.exists()) continue;

                try (FileInputStream fis = new FileInputStream(f)) {
                    ZipEntry zipEntry = new ZipEntry(f.getName());
                    zos.putNextEntry(zipEntry);
                    byte[] buffer = new byte[8192];
                    int length;
                    while ((length = fis.read(buffer)) >= 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                }
            }
        }
    }
}
