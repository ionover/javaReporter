package ru.mycrg.jasperreporte;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    private static final Path OUTPUT_DIR = Paths.get("files");

    public void saveFile(byte[] pdfBytes) {
        try {
            // создаём папку, если её ещё нет
            Files.createDirectories(OUTPUT_DIR);

            String fileName = "report-" + System.currentTimeMillis() + ".pdf";
            Path filePath = OUTPUT_DIR.resolve(fileName);

            Files.write(filePath, pdfBytes);
            System.out.println("PDF сохранён в файл: " + filePath.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить PDF-файл", e);
        }
    }
}
