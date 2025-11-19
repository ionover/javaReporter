package ru.mycrg.jasperreporte;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JasperController {

    private final JasperService jasperService;
    private final FileService fileService;

    public JasperController(JasperService jasperService, FileService fileService) {
        this.jasperService = jasperService;
        this.fileService = fileService;
    }

    @GetMapping(value = "/report", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getResponse(@RequestParam String text) {
        // сервис должен вернуть готовый PDF как массив байт
        byte[] pdfBytes = jasperService.createPdf(text);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/test")
    public void createFile(@RequestParam String text) {
        // сервис должен вернуть готовый PDF как массив байт
        byte[] pdfBytes = jasperService.createPdf(text);

        fileService.saveFile(pdfBytes);
    }
}
