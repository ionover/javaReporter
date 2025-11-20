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
     * Класс для передачи координат в отчёт JasperReports
     * Нужен для связи с полями в JRXML (number, x, y)
     */
    public static class Coordinate {
        private Integer number;
        private String x;
        private String y;

        public Coordinate(Integer number, String x, String y) {
            this.number = number;
            this.x = x;
            this.y = y;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }
    }

    /**
     * Основной метод - работает с твоим ReportDto
     */
    public byte[] createPdf(ReportDto reportDto) {
        if (reportDto == null || reportDto.d == null) {
            throw new IllegalArgumentException("ReportDto или ClientData не должны быть null");
        }

        ClientData data = reportDto.d;

        // Конвертируем координаты из твоего DTO в формат для JasperReports
        List<Coordinate> coordinates = convertCoordinates(data.getCoordinates());

        return createPdf(
            data.getColumn(),  // CELL1
            data.getValue(),   // CELL2
            data.getCrs(),     // PRICE (или можешь использовать для другого поля)
            data.getArea(),    // AREA
            coordinates
        );
    }

    /**
     * Конвертирует твои CoordinatesDto в Coordinate для JasperReports
     */
    private List<Coordinate> convertCoordinates(List<CoordinatesDto> dtoList) {
        List<Coordinate> result = new ArrayList<>();
        if (dtoList != null) {
            for (int i = 0; i < dtoList.size(); i++) {
                CoordinatesDto dto = dtoList.get(i);
                result.add(new Coordinate(i + 1, dto.getX(), dto.getY()));
            }
        }
        return result;
    }

    /**
     * Низкоуровневый метод создания PDF
     */
    public byte[] createPdf(String cell1, String cell2, String price, String area, List<Coordinate> coordinates) {
        try (InputStream templateStream = getClass().getResourceAsStream("/report_new.jrxml")) {
            if (templateStream == null) {
                throw new IllegalStateException("Не найден шаблон /report_new.jrxml в classpath");
            }

            // Компилируем JRXML в JasperReport
            JasperReport jasperReport = JasperCompileManager.compileReport(templateStream);

            // Подготавливаем параметры
            Map<String, Object> params = new HashMap<>();
            params.put("CELL1", cell1);
            params.put("CELL2", cell2);
            params.put("PRICE", price);
            params.put("AREA", area);
            
            // Путь к картинке в classpath
            params.put("IMAGE_PATH", getClass().getResourceAsStream("/flower1.png"));

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
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(1, "55.7558", "37.6173")); // Москва
        
        return createPdf(
            "Значение 1", 
            "Значение 2",
            "1000000 руб.", 
            "500 м²",
            coordinates
        );
    }

    /**
     * Пример использования - создание отчёта для полигона (много координат)
     */
    public byte[] createPolygonReport() {
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(1, "55.7558", "37.6173"));
        coordinates.add(new Coordinate(2, "55.7559", "37.6174"));
        coordinates.add(new Coordinate(3, "55.7560", "37.6175"));
        coordinates.add(new Coordinate(4, "55.7561", "37.6176"));
        coordinates.add(new Coordinate(5, "55.7562", "37.6177"));
        coordinates.add(new Coordinate(6, "55.7563", "37.6178"));
        coordinates.add(new Coordinate(7, "55.7564", "37.6179"));
        coordinates.add(new Coordinate(8, "55.7565", "37.6180"));
        coordinates.add(new Coordinate(9, "55.7566", "37.6181"));
        coordinates.add(new Coordinate(10, "55.7567", "37.6182"));
        
        return createPdf(
            "Полигон А", 
            "Зона Б",
            "5000000 руб.", 
            "2500 м²",
            coordinates
        );
    }
}
