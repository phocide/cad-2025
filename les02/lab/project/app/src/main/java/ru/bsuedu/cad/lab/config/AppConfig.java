package ru.bsuedu.cad.lab.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/*
 * Класс конфигурации Spring.
 * Аннотация @Configuration указывает, что класс содержит определения бинов.
 * Аннотация @ComponentScan ищет компоненты (билды, сервисы и т.д.) в указанном пакете.
 */
@Configuration
@ComponentScan(basePackages = "ru.bsuedu.cad.lab")
public class AppConfig {
} 