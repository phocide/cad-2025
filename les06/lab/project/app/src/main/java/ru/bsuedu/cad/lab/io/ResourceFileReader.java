package ru.bsuedu.cad.lab.io;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import jakarta.annotation.PostConstruct;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class ResourceFileReader implements Reader {
    // ResourceLoader используется для загрузки ресурсов из classpath.
    private final ResourceLoader resourceLoader;
    
    // Получаем путь к файлу из конфигурации с помощью SpEL
    @Value("${csv.file.path}")
    private String csvFilePath;

    private final String explicitFilePath;

    public ResourceFileReader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.explicitFilePath = null;
    }

    public ResourceFileReader(ResourceLoader resourceLoader, String explicitFilePath) {
        this.resourceLoader = resourceLoader;
        this.explicitFilePath = explicitFilePath;
    }
    
    // Метод жизненного цикла, выполняется после инициализации бина
    @PostConstruct
    public void init() {
        System.out.println("ResourceFileReader инициализирован в: " + LocalDateTime.now());
    }

    @Override
    public String read() {
        String path = explicitFilePath != null ? explicitFilePath : csvFilePath;
        Resource resource = resourceLoader.getResource("classpath:" + path);
        // Указываем кодировку UTF-8 при чтении файла
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            // Читаем все строки файла и объединяем их в одну строку с разделителем переноса строки.
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            // В случае ошибки ввода-вывода выбрасываем RuntimeException.
            throw new RuntimeException("Не удалось прочитать файл: " + path, e);
        }
    }
} 