package ru.bsuedu.cad.lab.io;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
public class ResourceFileReader implements Reader {
    // ResourceLoader используется для загрузки ресурсов из classpath.
    private final ResourceLoader resourceLoader;
    
    // Получаем путь к файлу из конфигурации с помощью SpEL
    @Value("${csv.file.path}")
    private String csvFilePath;

    public ResourceFileReader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    
    // Метод жизненного цикла, выполняется после инициализации бина
    @PostConstruct
    public void init() {
        System.out.println("ResourceFileReader инициализирован в: " + LocalDateTime.now());
    }

    @Override
    public String read() {
        // Загружаем CSV файл по пути из конфигурации
        Resource resource = resourceLoader.getResource("classpath:" + csvFilePath);
        // Указываем кодировку UTF-8 при чтении файла
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            // Читаем все строки файла и объединяем их в одну строку с разделителем переноса строки.
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            // В случае ошибки ввода-вывода выбрасываем RuntimeException.
            throw new RuntimeException("Не удалось прочитать файл: " + csvFilePath, e);
        }
    }
} 