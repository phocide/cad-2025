package ru.bsuedu.cad.lab.renderer;

import ru.bsuedu.cad.lab.model.Product;
import ru.bsuedu.cad.lab.provider.ProductProvider;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ConsoleTableRenderer implements Renderer {
    // Компонент для получения списка продуктов.
    private final ProductProvider productProvider;
    // Используем форматтер только для даты
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // PrintStream, настроенный на вывод в UTF-8.
    private final PrintStream out;

    public ConsoleTableRenderer(ProductProvider productProvider) {
        this.productProvider = productProvider;
        // Настраиваем вывод в консоль с использованием UTF-8
        this.out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    }

    @Override
    public void render() {
        // Получаем список продуктов через ProductProvider.
        List<Product> products = productProvider.getProducts();
        
        // Корректируем ширину столбцов для даты (уменьшаем, так как убрали время)
        out.println("+-----------+-----------------------+-----------------------+-------------+----------+----------------+----------------------+------------+------------+");
        out.println("| ProductID | Name                  | Description           | CategoryID  | Price    | StockQuantity  | ImageURL             | CreatedAt  | UpdatedAt  |");
        out.println("+-----------+-----------------------+-----------------------+-------------+----------+----------------+----------------------+------------+------------+");
        
        // Цикл по каждому продукту для вывода его данных в виде строки таблицы.
        for (Product product : products) {
            out.printf("| %-9d | %-21s | %-21s | %-11d | %8.2f | %-14d | %-20s | %-10s | %-10s |\n",
                    product.getProductId(), // Идентификатор продукта
                    truncateString(product.getName(), 21), // Название продукта (если слишком длинное, оно укорачивается)
                    truncateString(product.getDescription(), 21), // Описание продукта (укорачивание, если необходимо)
                    product.getCategoryId(), // Идентификатор категории
                    product.getPrice(), // Цена продукта
                    product.getStockQuantity(), // Количество на складе
                    truncateString(product.getImageUrl(), 20), // URL изображения (обрезается, если длинный)
                    dateFormatter.format(product.getCreatedAt().toLocalDate()),  // Преобразуем в LocalDate и форматируем
                    dateFormatter.format(product.getUpdatedAt().toLocalDate())   // Преобразуем в LocalDate и форматируем
            );
        }
        
        // Выводим нижнюю границу таблицы.
        out.println("+-----------+-----------------------+-----------------------+-------------+----------+----------------+----------------------+------------+------------+");
        
        // Выводим общее количество строк (продуктов) в наборе.
        out.printf("%d rows in set\n", products.size());
    }

    // Вспомогательный метод для обрезки строки, если её длина превышает указанное значение.
    // Если строка длиннее, то она укорачивается и в конце добавляется троеточие.
    private String truncateString(String str, int length) {
        if (str == null) return "";
        return str.length() > length ? str.substring(0, length - 3) + "..." : str;
    }
} 