package ru.bsuedu.cad.lab.parser;

import ru.bsuedu.cad.lab.model.Product;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CSVParser implements Parser {
    
    @PostConstruct
    public void init() {
        System.out.println("CSVParser инициализирован в: " + LocalDateTime.now());
    }

    @Override
    public List<Product> parse(String content) {
        System.out.println("Парсим CSV...");
        // Разбиваем содержимое CSV файла на строки, пропускаем 1-ю строку (заголовок)
        // и преобразуем каждую строку в объект Product.
        return Arrays.stream(content.split("\n"))
            .skip(1) // Пропустить строку заголовка
            .map(line -> {
                // Разбиваем строку на части по разделителю запятая
                String[] parts = line.split(",");
                // Создаем объект Product с разобранными данными
                return new Product(
                        Integer.parseInt(parts[0]),             // productId
                        parts[1],                               // name
                        parts[2],                               // description
                        Integer.parseInt(parts[3]),             // categoryId
                        Double.parseDouble(parts[4]),           // price
                        Integer.parseInt(parts[5]),             // stockQuantity
                        parts[6],                               // imageUrl
                        // Парсим только дату, преобразуем в LocalDateTime
                        LocalDateTime.parse(parts[7] + "T00:00:00"),
                        LocalDateTime.parse(parts[8] + "T00:00:00")
                );
            })
            .collect(Collectors.toList());
    }
} 