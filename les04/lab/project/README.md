# Отчет о лабораторной работе 2

## Цель работы
- Переконфигурирование приложения Spring c помощью аннотаций
- Применение аспектно-ориентированного программирования (АОП) для логирования и измерения производительности
- Создание HTML-представления таблицы с товарами

## Выполнение работы
### 1. Перенос проекта из предыдущей лабораторной работы:
- Скопирован проект из лабораторной работы №1 в директорию `/les04/lab/`
- Сохранена вся структура проекта и зависимости

### 2. Переконфигурирование приложения с помощью аннотаций:
- Заменены конфигурации в XML на аннотации `@Component` для всех компонентов приложения
- Добавлена конфигурация с помощью класса `AppConfig` с аннотацией `@Configuration`
- Настроено автоматическое сканирование компонентов с помощью `@ComponentScan`

### 3. Настройка загрузки параметров из файла конфигурации:
- Создан конфигурационный файл `application.properties` в каталоге ресурсов
- Добавлено получение имени файла для загрузки продуктов с помощью аннотации `@Value` и SpEL
```java
@Value("${csv.file.path}")
private String csvFilePath;
```

### 4. Добавление HTML-рендерера таблицы:
- Реализован новый класс `HTMLTableRenderer`, реализующий интерфейс `Renderer`
- Добавлена аннотация `@Primary` для приоритетного использования HTML-рендерера
- Настроено форматирование HTML-таблицы со стилями CSS

### 5. Отслеживание жизненного цикла бинов:
- Добавлен метод инициализации для бина `ResourceFileReader` с аннотацией `@PostConstruct`
- Реализован вывод в консоль даты и времени инициализации бина

### 6. Применение АОП для измерения производительности:
- Добавлены зависимости для работы с АОП Spring
- Создан аспект `PerformanceAspect` с аннотацией `@Aspect`
- Реализован метод для измерения времени выполнения парсинга CSV-файла:
```java
@Around("execution(* ru.bsuedu.cad.lab.parser.CSVParser.parse(..))")
public Object measureParsingTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long startTime = System.currentTimeMillis();
    Object result = joinPoint.proceed();
    long endTime = System.currentTimeMillis();
    System.out.println("Время парсинга CSV-файла: " + (endTime - startTime) + " мс");
    return result;
}
```

### 7. Запуск приложения:
- Приложение успешно запускается командой `gradle run`
- Выводит информацию в консоль о времени парсинга и генерирует HTML-файл
- HTML-таблица успешно создается и отображает данные о продуктах

![HTML таблица продуктов](image.png)

## UML-диаграмма классов

```mermaid
classDiagram
    class App {
        +main(args: String[]) void
    }
    
    class AppConfig {
        <<Configuration>>
        +componentScan()
    }
    
    class Renderer {
        <<Interface>>
        +render() void
    }
    
    class ConsoleTableRenderer {
        <<Component>>
        -productProvider: ProductProvider
        -out: PrintStream
        +render() void
        -truncateString(str: String, length: int) String
    }
    
    class HTMLTableRenderer {
        <<Component>>
        <<Primary>>
        -productProvider: ProductProvider
        -HTML_FILE_PATH: String
        +render() void
    }
    
    class ProductProvider {
        <<Interface>>
        +getProducts() List~Product~
    }
    
    class ConcreteProductProvider {
        <<Component>>
        -reader: Reader
        -parser: Parser
        +getProducts() List~Product~
    }
    
    class Reader {
        <<Interface>>
        +read() String
    }
    
    class ResourceFileReader {
        <<Component>>
        -resourceLoader: ResourceLoader
        -csvFilePath: String
        +init() void
        +read() String
    }
    
    class Parser {
        <<Interface>>
        +parse(content: String) List~Product~
    }
    
    class CSVParser {
        <<Component>>
        +parse(content: String) List~Product~
    }
    
    class Product {
        -productId: int
        -name: String
        -description: String
        -categoryId: int
        -price: double
        -stockQuantity: int
        -imageUrl: String
        -createdAt: LocalDateTime
        -updatedAt: LocalDateTime
        +getProductId() int
        +getName() String
        +getDescription() String
        +getCategoryId() int
        +getPrice() double
        +getStockQuantity() int
        +getImageUrl() String
        +getCreatedAt() LocalDateTime
        +getUpdatedAt() LocalDateTime
    }
    
    class PerformanceAspect {
        <<Aspect>>
        <<Component>>
        +measureParsingTime(joinPoint: ProceedingJoinPoint) Object
    }
    
    App --> AppConfig
    Renderer <|-- ConsoleTableRenderer
    Renderer <|-- HTMLTableRenderer
    ConsoleTableRenderer --> ProductProvider
    HTMLTableRenderer --> ProductProvider
    ProductProvider <|-- ConcreteProductProvider
    ConcreteProductProvider --> Reader
    ConcreteProductProvider --> Parser
    Reader <|-- ResourceFileReader
    Parser <|-- CSVParser
    PerformanceAspect ..> CSVParser
```

## Выводы
1. Успешно переконфигурировано Spring-приложение с использованием аннотаций, что упростило настройку компонентов
2. Освоены принципы внедрения значений из конфигурационных файлов с помощью `@Value` и SpEL
3. Изучен жизненный цикл бинов Spring и применение аннотаций жизненного цикла
4. Реализовано применение аспектно-ориентированного программирования для логирования и измерения производительности
5. Добавлена возможность представления данных в формате HTML-таблицы, что расширяет функциональность приложения
6. Приложение успешно запускается и выполняет все поставленные задачи