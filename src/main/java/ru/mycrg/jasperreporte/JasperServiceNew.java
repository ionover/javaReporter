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
            JasperReport jasperReport = null;
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

    /**
     * Пример использования - создание отчёта для точки (1 координата)
     */
    public byte[] createPointReport() {
        List<CoordinatesDto> coordinates = new ArrayList<>();
        coordinates.add(new CoordinatesDto(1, 55.7558, 37.6173)); // Москва

        return createPdf(
                "Тестовый отчёт",
                null,
                "Значение 1",
                "Значение 2",
                "EPSG:4326",
                500.0,
                coordinates
        );
    }

    /**
     * Пример использования - создание отчёта для полигона (много координат)
     */
    public byte[] createPolygonReport() {
        List<CoordinatesDto> coordinates = new ArrayList<>();
        coordinates.add(new CoordinatesDto(1, 55.7558, 37.6173));
        coordinates.add(new CoordinatesDto(2, 55.7559, 37.6174));
        coordinates.add(new CoordinatesDto(3, 55.7560, 37.6175));
        coordinates.add(new CoordinatesDto(4, 55.7561, 37.6176));
        coordinates.add(new CoordinatesDto(5, 55.7562, 37.6177));
        coordinates.add(new CoordinatesDto(6, 55.7563, 37.6178));
        coordinates.add(new CoordinatesDto(7, 55.7564, 37.6179));
        coordinates.add(new CoordinatesDto(8, 55.7565, 37.6180));
        coordinates.add(new CoordinatesDto(9, 55.7566, 37.6181));
        coordinates.add(new CoordinatesDto(10, 55.7567, 37.6182));

        return createPdf(
                "Тестовый отчёт по полигону",
                null,
                "Полигон А",
                "Зона Б",
                "EPSG:4326",
                2500.0,
                coordinates
        );
    }
}
