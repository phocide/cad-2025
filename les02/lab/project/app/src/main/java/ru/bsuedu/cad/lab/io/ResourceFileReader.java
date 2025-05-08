package ru.bsuedu.cad.lab.io;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
public class ResourceFileReader implements Reader {
    // ResourceLoader используется для загрузки ресурсов из classpath.
    private final ResourceLoader resourceLoader;

    public ResourceFileReader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public String read() {
        // Загружаем CSV файл по пути: classpath:product.csv.
        Resource resource = resourceLoader.getResource("classpath:product.csv");
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            // Читаем все строки файла и объединяем их в одну строку с разделителем переноса строки.
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            // В случае ошибки ввода-вывода выбрасываем RuntimeException.
            throw new RuntimeException("Failed to read file", e);
        }
    }
} 