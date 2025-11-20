# Быстрый старт с новым JRXML шаблоном

## 🎯 Что создано

✅ **report_new.jrxml** - готовый валидный JRXML шаблон  
✅ **JasperServiceNew.java** - пример сервиса для работы с шаблоном  
✅ **JASPER_GUIDE.md** - подробное руководство  
✅ **EXAMPLES.md** - примеры кода  

## 🚀 Как начать работать

### Шаг 1: Используй новый шаблон

Замени в своём коде:
```java
// Было:
InputStream templateStream = getClass().getResourceAsStream("/report.jrxml");

// Стало:
InputStream templateStream = getClass().getResourceAsStream("/report_new.jrxml");
```

### Шаг 2: Подготовь данные

```java
// Создай класс для координат (или используй готовый из JasperServiceNew)
public class Coordinate {
    private Integer number;
    private String x;
    private String y;
    // + конструктор и геттеры/сеттеры
}

// Создай список координат
List<Coordinate> coords = new ArrayList<>();
coords.add(new Coordinate(1, "55.7558", "37.6173"));
coords.add(new Coordinate(2, "55.7559", "37.6174"));
```

### Шаг 3: Передай параметры

```java
Map<String, Object> params = new HashMap<>();
params.put("CELL1", "Первое значение");
params.put("CELL2", "Второе значение");
params.put("PRICE", "1 000 000 руб.");
params.put("AREA", "500 м²");
params.put("IMAGE_PATH", getClass().getResourceAsStream("/flower1.png"));

JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(coords);
```

### Шаг 4: Генерируй PDF

```java
JasperReport report = JasperCompileManager.compileReport(templateStream);
JasperPrint print = JasperFillManager.fillReport(report, params, dataSource);
byte[] pdf = JasperExportManager.exportReportToPdf(print);
```

## 📋 Список параметров

| Параметр | Тип | Описание | Обязательный |
|----------|-----|----------|--------------|
| CELL1 | String | Значение в первой ячейке таблицы | Нет |
| CELL2 | String | Значение во второй ячейке таблицы | Нет |
| PRICE | String | Значение цены | Нет |
| AREA | String | Значение площади | Нет |
| IMAGE_PATH | InputStream/String | Путь к изображению | Да (по умолчанию /flower1.png) |

## 📊 Данные для таблицы координат

Передаются через `JRBeanCollectionDataSource` с полями:
- `number` (Integer) - номер точки
- `x` (String) - координата X
- `y` (String) - координата Y

## 🎨 Структура отчёта

```
┌─────────────────────────────────────┐
│  Это тестовый шаблон отчёта        │ ← Страница 1
│                                     │
│         [Картинка]                  │
│                                     │
│  ┌─────────┬─────────┐             │
│  │ Ячейка1 │ Ячейка2 │             │
│  ├─────────┼─────────┤             │
│  │ CELL1   │ CELL2   │             │
│  └─────────┴─────────┘             │
└─────────────────────────────────────┘
              ↓ (разрыв страницы)
┌─────────────────────────────────────┐
│     Ведомость координат            │ ← Страница 2
│                                     │
│  Цена: _____                       │
│  Площадь: _____                    │
│                                     │
│  ┌───┬─────────┬─────────┐        │
│  │ № │    X    │    Y    │        │
│  ├───┼─────────┼─────────┤        │
│  │ 1 │ 55.7558 │ 37.6173 │        │
│  │ 2 │ 55.7559 │ 37.6174 │        │
│  │...│   ...   │   ...   │        │
│  └───┴─────────┴─────────┘        │
└─────────────────────────────────────┘
```

## 🔧 Частые задачи

### Заменить картинку
Положи свою картинку в `src/main/resources/` и передай:
```java
params.put("IMAGE_PATH", getClass().getResourceAsStream("/твоя_картинка.png"));
```

### Изменить размер картинки
В файле `report_new.jrxml` найди:
```xml
<image scaleImage="RetainShape" hAlign="Center">
    <reportElement x="127" y="50" width="300" height="200"/>
    <!-- измени width и height -->
</image>
```

### Добавить больше ячеек в первую таблицу
1. Добавь параметры `CELL3`, `CELL4` и т.д. в JRXML
2. Скопируй блок `<textField>` и измени координаты `x` и параметр

### Изменить текст заголовков
В JRXML найди нужный `<staticText>` и измени:
```xml
<text><![CDATA[Твой новый текст]]></text>
```

## 🧪 Тестирование

Используй готовые методы из `JasperServiceNew`:
```java
JasperServiceNew service = new JasperServiceNew();

// Для одной точки
byte[] pdf1 = service.createPointReport();

// Для полигона (10 точек)
byte[] pdf2 = service.createPolygonReport();
```

## 📁 Расположение файлов

```
workspace/
├── src/main/resources/
│   ├── report_new.jrxml        ← Новый шаблон (используй этот!)
│   ├── report.jrxml            ← Старый шаблон (не трогай)
│   └── flower1.png             ← Картинка для отчёта
├── src/main/java/ru/mycrg/jasperreporte/
│   ├── JasperServiceNew.java   ← Новый сервис (пример использования)
│   └── JasperService.java      ← Старый сервис
├── JASPER_GUIDE.md             ← Подробное руководство
├── EXAMPLES.md                 ← Примеры кода
└── QUICK_START.md              ← Этот файл
```

## ❓ Возникли проблемы?

1. **Отчёт не компилируется** → Проверь валидность XML в JRXML файле
2. **Пустая таблица** → Проверь что список координат не пустой
3. **Параметр null** → Добавь параметр в Map или используй `isBlankWhenNull="true"`
4. **Картинка не отображается** → Проверь что файл существует в resources

## 💡 Полезные ссылки

- **Документация JasperReports**: https://jasperreports.sourceforge.net/
- **JRXML Schema**: http://jasperreports.sourceforge.net/xsd/jasperreport.xsd
- **Community**: https://community.jaspersoft.com/

---

**Готово к использованию! 🎉**

Шаблон полностью рабочий и валидный. Просто следуй инструкциям выше и всё заработает.
