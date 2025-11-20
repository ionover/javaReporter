# Руководство по использованию нового JRXML шаблона

## Файлы

- **report_new.jrxml** - новый шаблон отчёта
- **JasperServiceNew.java** - пример сервиса для работы с шаблоном

## Структура отчёта

### Страница 1

1. **Заголовок**: "Это тестовый шаблон отчёта"
2. **Картинка**: flower1.png (300x200 px, по центру)
3. **Таблица 2x2**: 
   - Заголовки: "Ячейка 1", "Ячейка 2"
   - Значения: параметры `CELL1` и `CELL2`
4. **Разрыв страницы**

### Страница 2

1. **Заголовок**: "Ведомость координат"
2. **Текстовые поля**:
   - Цена: параметр `PRICE`
   - Площадь: параметр `AREA`
3. **Таблица координат**: колонки №, X, Y (динамическая)

## Параметры шаблона

### Простые параметры (передаются в Map)

- `CELL1` - значение для первой ячейки таблицы
- `CELL2` - значение для второй ячейки таблицы
- `PRICE` - значение цены
- `AREA` - значение площади
- `IMAGE_PATH` - путь к изображению (по умолчанию "/flower1.png")

### Данные таблицы координат

Таблица использует `JRBeanCollectionDataSource` с объектами класса `Coordinate`:
```java
public class Coordinate {
    private Integer number;  // Номер точки
    private String x;        // Координата X
    private String y;        // Координата Y
    
    // getters/setters
}
```

## Пример использования

```java
// Создаём список координат
List<Coordinate> coordinates = new ArrayList<>();
coordinates.add(new Coordinate(1, "55.7558", "37.6173"));
coordinates.add(new Coordinate(2, "55.7559", "37.6174"));
// ... добавляем сколько нужно

// Подготавливаем параметры
Map<String, Object> params = new HashMap<>();
params.put("CELL1", "Значение 1");
params.put("CELL2", "Значение 2");
params.put("PRICE", "1000000 руб.");
params.put("AREA", "500 м²");
params.put("IMAGE_PATH", getClass().getResourceAsStream("/flower1.png"));

// Создаём datasource для таблицы
JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(coordinates);

// Компилируем и заполняем отчёт
JasperReport jasperReport = JasperCompileManager.compileReport(templateStream);
JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

// Экспортируем в PDF
byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
```

## Как изменить шаблон

### Заменить картинку

1. Положи новую картинку в `src/main/resources/`
2. В коде измени параметр:
```java
params.put("IMAGE_PATH", getClass().getResourceAsStream("/твоя_картинка.png"));
```

Или измени в JRXML файле значение по умолчанию:
```xml
<parameter name="IMAGE_PATH" class="java.lang.String" isForPrompting="false">
    <defaultValueExpression><![CDATA["/твоя_картинка.png"]]></defaultValueExpression>
</parameter>
```

### Изменить размер картинки

В JRXML найди элемент `<image>` и измени параметры:
```xml
<image scaleImage="RetainShape" hAlign="Center">
    <reportElement x="127" y="50" width="300" height="200"/>
    <!-- width и height - размеры -->
    <!-- x и y - позиция -->
</image>
```

### Изменить текст заголовков

Найди нужный `<staticText>` и измени содержимое `<text><![CDATA[...]]></text>`

### Добавить/удалить колонки в таблице координат

1. Добавь поле в класс `Coordinate`
2. В JRXML добавь `<field>`:
```xml
<field name="новое_поле" class="java.lang.String"/>
```
3. Добавь колонку в `<columnHeader>` (заголовок)
4. Добавь соответствующий `<textField>` в `<detail>` (строка данных)

### Изменить ширину колонок таблицы

В элементах найди атрибут `width` и измени значение:
```xml
<reportElement x="100" y="0" width="200" height="25"/>
```

## Размеры страницы

- Страница A4: 595x842 точек
- Поля: 20 точек со всех сторон
- Рабочая область: 555 точек в ширину

## Важные замечания

1. **Координаты элементов** задаются через `x` и `y` в `<reportElement>`
2. **Размеры** задаются через `width` и `height`
3. **Рамки** создаются через `<box>` с настройками `<pen>`
4. **Разрыв страницы** делается элементом `<break>`
5. **Условное отображение** через `<printWhenExpression>`
6. Заголовок второй страницы показывается только если `PAGE_NUMBER > 1`

## Полезные настройки

### Выравнивание текста
```xml
<textElement textAlignment="Center" verticalAlignment="Middle">
```
Варианты: `Left`, `Center`, `Right`, `Justified` / `Top`, `Middle`, `Bottom`

### Жирный шрифт
```xml
<font size="12" isBold="true"/>
```

### Цвет фона
```xml
<reportElement mode="Opaque" backcolor="#E0E0E0" .../>
```

### Рамки с разными сторонами
```xml
<box>
    <pen lineWidth="1.0"/>
    <topPen lineWidth="2.0"/>
    <leftPen lineWidth="1.0"/>
    <bottomPen lineWidth="1.0" lineStyle="Dotted"/>
    <rightPen lineWidth="1.0"/>
</box>
```

## Тестирование

Используй примеры в `JasperServiceNew.java`:
- `createPointReport()` - для одной точки
- `createPolygonReport()` - для полигона (10 точек)

Или создай свой метод с нужными данными.
