package ru.bsuedu.cad.lab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

import ru.bsuedu.cad.lab.renderer.ConsoleTableRenderer;
import ru.bsuedu.cad.lab.renderer.HTMLTableRenderer;
import ru.bsuedu.cad.lab.renderer.Renderer;
import ru.bsuedu.cad.lab.provider.ProductProvider;

/*
 * Класс конфигурации Spring.
 * Аннотация @Configuration указывает, что класс содержит определения бинов.
 * Аннотация @ComponentScan ищет компоненты (билды, сервисы и т.д.) в указанном пакете.
 * Аннотация @EnableAspectJAutoProxy включает поддержку АОП для Spring.
 * Аннотация @PropertySource позволяет загружать свойства из файла application.properties.
 */
@Configuration
@ComponentScan(basePackages = "ru.bsuedu.cad.lab")
@EnableAspectJAutoProxy
@PropertySource("classpath:application.properties")
public class AppConfig {
    
    // Свойство для хранения выбранного типа рендерера
    private static String rendererType = "html"; // По умолчанию используем HTML рендерер
    
    // Метод для установки типа рендерера
    public static void setRendererType(String type) {
        System.out.println("Установка типа рендерера: " + type);
        if ("console".equalsIgnoreCase(type) || "html".equalsIgnoreCase(type)) {
            rendererType = type.toLowerCase();
            System.out.println("Тип рендерера установлен: " + rendererType);
        } else {
            System.out.println("Неизвестный тип рендерера: " + type + ". Используется HTML рендерер по умолчанию.");
        }
    }
    
    @Bean
    public Renderer renderer(ProductProvider productProvider) {
        System.out.println("Создание рендерера на основе типа: " + rendererType);
        if ("console".equals(rendererType)) {
            System.out.println("Создаю ConsoleTableRenderer");
            return new ConsoleTableRenderer(productProvider);
        } else {
            System.out.println("Создаю HTMLTableRenderer");
            return new HTMLTableRenderer(productProvider);
        }
    }
} 