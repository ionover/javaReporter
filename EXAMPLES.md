# Примеры работы с JRXML шаблоном

## Вариант 1: Использование JRBeanCollectionDataSource (рекомендуется)

Это основной способ, который используется в `report_new.jrxml` и `JasperServiceNew.java`.

### Преимущества:
- Простота использования
- Типобезопасность (Java классы)
- Автоматическая работа с коллекциями
- Легко тестировать

### Пример кода:

```java
List<Coordinate> coordinates = new ArrayList<>();
coordinates.add(new Coordinate(1, "55.7558", "37.6173"));
coordinates.add(new Coordinate(2, "55.7559", "37.6174"));

Map<String, Object> params = new HashMap<>();
params.put("CELL1", "Тест 1");
params.put("CELL2", "Тест 2");
params.put("PRICE", "1000000");
params.put("AREA", "500");

JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(coordinates);
JasperPrint print = JasperFillManager.fillReport(report, params, dataSource);
```

## Вариант 2: Использование пустого DataSource с subdataset

Если тебе нужен другой подход, где таблица - это отдельный параметр.

### Код в Java:

```java
List<Coordinate> coordinates = new ArrayList<>();
// заполняем координаты...

Map<String, Object> params = new HashMap<>();
params.put("CELL1", "Тест 1");
params.put("CELL2", "Тест 2");
params.put("PRICE", "1000000");
params.put("AREA", "500");
params.put("COORDINATES_DATA", new JRBeanCollectionDataSource(coordinates));

JREmptyDataSource emptyDataSource = new JREmptyDataSource();
JasperPrint print = JasperFillManager.fillReport(report, params, emptyDataSource);
```

## Интеграция с контроллером

```java
@RestController
@RequestMapping("/api/reports")
public class ReportController {
    
    @Autowired
    private JasperServiceNew jasperService;
    
    @PostMapping("/create")
    public ResponseEntity<byte[]> createReport(@RequestBody ReportRequest request) {
        byte[] pdf = jasperService.createPdf(
            request.getCell1(),
            request.getCell2(),
            request.getPrice(),
            request.getArea(),
            request.getCoordinates()
        );
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "report.pdf");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(pdf);
    }
    
    @GetMapping("/test-point")
    public ResponseEntity<byte[]> testPointReport() {
        byte[] pdf = jasperService.createPointReport();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "point-report.pdf");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(pdf);
    }
    
    @GetMapping("/test-polygon")
    public ResponseEntity<byte[]> testPolygonReport() {
        byte[] pdf = jasperService.createPolygonReport();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "polygon-report.pdf");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(pdf);
    }
}

@Data
class ReportRequest {
    private String cell1;
    private String cell2;
    private String price;
    private String area;
    private List<JasperServiceNew.Coordinate> coordinates;
}
```

## Работа с изображениями

### Вариант 1: Из classpath (как в примере)
```java
params.put("IMAGE_PATH", getClass().getResourceAsStream("/flower1.png"));
```

### Вариант 2: Из файловой системы
```java
params.put("IMAGE_PATH", "/path/to/image.png");
```

В JRXML используй:
```xml
<imageExpression><![CDATA[$P{IMAGE_PATH}]]></imageExpression>
```

### Вариант 3: Передать как byte[]
```java
byte[] imageBytes = Files.readAllBytes(Paths.get("/path/to/image.png"));
params.put("IMAGE_BYTES", imageBytes);
```

В JRXML:
```xml
<imageExpression class="byte[]"><![CDATA[$P{IMAGE_BYTES}]]></imageExpression>
```

## Типичные ошибки и решения

### Ошибка: "Field not found"
**Причина**: Поле объявлено в JRXML, но нет в Java классе или наоборот.
**Решение**: Проверь что имена полей совпадают и есть геттеры.

### Ошибка: "Parameter is null"
**Причина**: Параметр не передан в Map.
**Решение**: Добавь параметр в Map или используй `isBlankWhenNull="true"` в JRXML.

### Ошибка: Пустая таблица
**Причина**: DataSource пустой или не правильно передан.
**Решение**: Проверь что список координат не пустой и правильно создан JRBeanCollectionDataSource.

### Ошибка: "Cannot parse"
**Причина**: Синтаксическая ошибка в JRXML.
**Решение**: Проверь XML на правильность закрытия тегов и структуру.

## Полезные трюки

### Пронумеровать строки автоматически
Используй переменную `$V{REPORT_COUNT}` вместо `$F{number}`:
```xml
<textFieldExpression><![CDATA[$V{REPORT_COUNT}]]></textFieldExpression>
```

### Альтернативный цвет строк (zebra stripes)
```xml
<reportElement mode="Opaque" ...>
    <printWhenExpression><![CDATA[$V{REPORT_COUNT} % 2 == 0]]></printWhenExpression>
</reportElement>
```

### Форматирование чисел
```xml
<textField pattern="#,##0.00">
    <textFieldExpression><![CDATA[$F{price}]]></textFieldExpression>
</textField>
```

### Форматирование дат
```xml
<textField pattern="dd.MM.yyyy">
    <textFieldExpression><![CDATA[$F{date}]]></textFieldExpression>
</textField>
```

### Итоговая сумма в footer
```xml
<variable name="SUM_X" class="java.lang.Double" calculation="Sum">
    <variableExpression><![CDATA[Double.valueOf($F{x})]]></variableExpression>
</variable>

<!-- В footer: -->
<textField>
    <textFieldExpression><![CDATA[$V{SUM_X}]]></textFieldExpression>
</textField>
```

## Компиляция JRXML заранее

Для ускорения работы можно скомпилировать JRXML в .jasper файл:

```java
InputStream jrxmlStream = getClass().getResourceAsStream("/report_new.jrxml");
JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

// Сохраняем скомпилированный файл
JRSaver.saveObject(jasperReport, "report_new.jasper");

// Потом загружаем уже скомпилированный
JasperReport report = (JasperReport) JRLoader.loadObject(
    getClass().getResourceAsStream("/report_new.jasper")
);
```

Это значительно ускоряет генерацию отчётов в production.
