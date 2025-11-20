package ru.mycrg.jasperreporte;

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
    public void createFile(@RequestBody ReportDto dto) {
        byte[] pdfBytes = jasperService.createPdf(dto);
        fileService.saveFile(pdfBytes);
    }
}
