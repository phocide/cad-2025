package ru.bsuedu.cad.lab.provider;

import ru.bsuedu.cad.lab.model.Product;

import java.util.List;

/*
 * Интерфейс ProductProvider определяет метод для получения списка продуктов.
 */
public interface ProductProvider {
    List<Product> getProducts();
} 