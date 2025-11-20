package ru.mycrg.jasperreporte;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    //Оставим чтобы не забыть реализовать сначала нужно научиться сохранять на машине
//    @GetMapping(value = "/report", produces = MediaType.APPLICATION_PDF_VALUE)
//    public ResponseEntity<byte[]> getResponse(@RequestParam String text) {
//        // сервис должен вернуть готовый PDF как массив байт
//        byte[] pdfBytes = jasperService.createPdf(text);
//
//        return ResponseEntity
//                .ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=report.pdf")
//                .contentType(MediaType.APPLICATION_PDF)
//                .body(pdfBytes);
//    }

    @GetMapping("/test")
    public void createFile(@RequestParam ReportDto dto) {
        // сервис должен вернуть готовый PDF как массив байт


        byte[] pdfBytes = jasperService.createPdf(dto);

        fileService.saveFile(pdfBytes);
    }
}
