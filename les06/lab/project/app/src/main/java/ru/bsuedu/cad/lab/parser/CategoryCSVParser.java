package ru.bsuedu.cad.lab.parser;

import ru.bsuedu.cad.lab.model.Category;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryCSVParser {
    @PostConstruct
    public void init() {
        System.out.println("CategoryCSVParser инициализирован в: " + LocalDateTime.now());
    }

    public List<Category> parse(String content) {
        System.out.println("Парсим CSV для категорий...");
        return Arrays.stream(content.split("\n"))
            .skip(1) // Пропустить строку заголовка
            .map(line -> {
                String[] parts = line.split(",");
                return new Category(
                        Integer.parseInt(parts[0]), // category_id
                        parts[1],                   // name
                        parts[2]                    // description
                );
            })
            .collect(Collectors.toList());
    }
} 