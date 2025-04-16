package ru.bsuedu.cad.lab.parser;

import ru.bsuedu.cad.lab.model.Product;

import java.util.List;

/*
 * Интерфейс Parser определяет метод для преобразования строки-данных (например, CSV) в список объектов Product.
 */
public interface Parser {
    List<Product> parse(String content);
} 