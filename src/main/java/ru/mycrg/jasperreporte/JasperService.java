package ru.mycrg.jasperreporte;

import net.sf.jasperreports.engine.*;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class JasperService {

    public byte[] createPdf(String text) {
        try (InputStream templateStream = getClass().getResourceAsStream("/report.jrxml")) {
            if (templateStream == null) {
                throw new IllegalStateException("Не найден шаблон /report.jrxml в classpath");
            }

            // Компилируем JRXML в JasperReport
            JasperReport jasperReport = JasperCompileManager.compileReport(templateStream);

            // Передаём текст как параметр в отчёт (предполагаем параметр TEXT в jrxml)
            Map<String, Object> params = new HashMap<>();
            params.put("TEXT", text);

            // Пустой datasource (нам достаточно параметров)
            JRDataSource dataSource = new JREmptyDataSource();

            // Заполняем отчёт
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            // Возвращаем PDF как byte[]
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при генерации PDF", e);
        }
    }
}
