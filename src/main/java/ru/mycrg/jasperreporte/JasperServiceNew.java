package ru.mycrg.jasperreporte;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import ru.mycrg.jasperreporte.dto.ClientData;
import ru.mycrg.jasperreporte.dto.CoordinatesDto;
import ru.mycrg.jasperreporte.dto.ReportDto;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JasperServiceNew {

    /**
     * Основной метод - работает с твоим ReportDto
     */
    public byte[] createPdf(ReportDto reportDto) {
        if (reportDto == null || reportDto.getD() == null) {
            throw new IllegalArgumentException("ReportDto или ClientData не должны быть null");
        }

        ClientData data = reportDto.getD();

        // Используем координаты напрямую из запроса
        List<CoordinatesDto> coordinates = data.getCoords() != null ? data.getCoords() : new ArrayList<>();

        return createPdf(
                data.getTitle(),
                data.getPicture(),
                data.getColumn(),
                data.getValue(),
                data.getCrs(),
                data.getArea(),
                coordinates
        );
    }

    /**
     * Низкоуровневый метод создания PDF
     */
    public byte[] createPdf(String title, String picture, String cell1, String cell2, String crs, Double area,
                            List<CoordinatesDto> coordinates) {
        try (InputStream templateStream = getClass().getResourceAsStream("/report_final.jrxml")) {
            if (templateStream == null) {
                throw new IllegalStateException("Не найден шаблон /report_final.jrxml в classpath");
            }

            // Компилируем JRXML в JasperReport
            JasperReport jasperReport;
            try {
                jasperReport = JasperCompileManager.compileReport(templateStream);
            } catch (Exception e) {
                System.err.println("Ошибка компиляции JRXML: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }

            // Подготавливаем параметры
            Map<String, Object> params = new HashMap<>();
            params.put("TITLE", title != null ? title : "");
            params.put("CELL1", cell1 != null ? cell1 : "");
            params.put("CELL2", cell2 != null ? cell2 : "");
            params.put("PRICE", crs != null ? crs : "");
            params.put("AREA", area != null ? String.format("%.2f", area) : "");

            // Используем картинку из запроса
            String imagePath = picture != null ? "/" + picture : "/flower1.png";
            params.put("IMAGE_PATH", getClass().getResourceAsStream(imagePath));

            // Создаём datasource для таблицы координат
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(coordinates);

            // Заполняем отчёт
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            // Возвращаем PDF как byte[]
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при генерации PDF: " + e.getMessage(), e);
        }
    }
}
