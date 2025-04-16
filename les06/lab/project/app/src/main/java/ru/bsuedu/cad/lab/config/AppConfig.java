package ru.bsuedu.cad.lab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import javax.sql.DataSource;

import ru.bsuedu.cad.lab.renderer.ConsoleTableRenderer;
import ru.bsuedu.cad.lab.renderer.HTMLTableRenderer;
import ru.bsuedu.cad.lab.renderer.DataBaseRenderer;
import ru.bsuedu.cad.lab.renderer.Renderer;
import ru.bsuedu.cad.lab.provider.ProductProvider;
import ru.bsuedu.cad.lab.provider.CategoryProvider;
import org.springframework.core.io.ResourceLoader;
import ru.bsuedu.cad.lab.io.ResourceFileReader;
import ru.bsuedu.cad.lab.io.Reader;
import ru.bsuedu.cad.lab.parser.Parser;
import ru.bsuedu.cad.lab.parser.CategoryCSVParser;
import org.springframework.beans.factory.annotation.Qualifier;

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
    private static String rendererType = "console"; // По умолчанию используем ConsoleTableRenderer
    
    // Метод для установки типа рендерера
    public static void setRendererType(String type) {
        System.out.println("Установка типа рендерера: " + type);
        if ("console".equalsIgnoreCase(type) || "html".equalsIgnoreCase(type) || "db".equalsIgnoreCase(type)) {
            rendererType = type.toLowerCase();
            System.out.println("Тип рендерера установлен: " + rendererType);
        } else {
            System.out.println("Неизвестный тип рендерера: " + type + ". Используется ConsoleTableRenderer по умолчанию.");
            rendererType = "console";
        }
    }
    
    @Bean
    public Renderer renderer(ProductProvider productProvider, CategoryProvider categoryProvider, DataSource dataSource) {
        System.out.println("Создание рендерера на основе типа: " + rendererType);
        if ("console".equals(rendererType)) {
            System.out.println("Создаю ConsoleTableRenderer");
            return new ConsoleTableRenderer(productProvider);
        } else if ("html".equals(rendererType)) {
            System.out.println("Создаю HTMLTableRenderer");
            return new HTMLTableRenderer(productProvider);
        } else {
            System.out.println("Создаю DataBaseRenderer (по умолчанию)");
            return new DataBaseRenderer(productProvider, categoryProvider, dataSource);
        }
    }

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema.sql")
                .build();
    }

    @Bean
    public Reader productReader(ResourceLoader resourceLoader) {
        return new ResourceFileReader(resourceLoader, "product.csv");
    }

    @Bean
    public Reader categoryReader(ResourceLoader resourceLoader) {
        return new ResourceFileReader(resourceLoader, "category.csv");
    }

    @Bean
    public ProductProvider productProvider(@Qualifier("productReader") Reader productReader, Parser parser) {
        return new ru.bsuedu.cad.lab.provider.ConcreteProductProvider(productReader, parser);
    }

    @Bean
    public CategoryProvider categoryProvider(@Qualifier("categoryReader") Reader categoryReader, CategoryCSVParser parser) {
        return new ru.bsuedu.cad.lab.provider.ConcreteCategoryProvider(categoryReader, parser);
    }
} 