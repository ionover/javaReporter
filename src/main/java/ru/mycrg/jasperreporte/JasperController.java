package ru.mycrg.jasperreporte;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.mycrg.jasperreporte.dto.ReportDto;

@RestController
public class JasperController {

    private final JasperServiceNew jasperService;
    private final FileService fileService;

    public JasperController(JasperServiceNew jasperService, FileService fileService) {
        this.jasperService = jasperService;
        this.fileService = fileService;
    }

    @PostMapping("/test")
    public ResponseEntity<byte[]> createFile(@RequestBody ReportDto dto) {
        byte[] pdfBytes = jasperService.createPdf(dto);
        fileService.saveFile(pdfBytes);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "report.pdf");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
