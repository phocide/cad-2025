package ru.bsuedu.cad.lab.renderer;

import ru.bsuedu.cad.lab.model.Product;
import ru.bsuedu.cad.lab.provider.ProductProvider;
import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class ConsoleTableRenderer implements Renderer {
    // Компонент для получения списка продуктов.
    private final ProductProvider productProvider;
    private final PrintStream out;

    public ConsoleTableRenderer(ProductProvider productProvider) {
        this.productProvider = productProvider;
        this.out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    }

    @Override
    public void render() {
        // Получаем список продуктов через ProductProvider.
        List<Product> products = productProvider.getProducts();
        
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
                    product.getCreatedAt().toLocalDate(),  // Используем только дату
                    product.getUpdatedAt().toLocalDate()   // Используем только дату
            );
        }
        
        // Выводим нижнюю границу таблицы.
        out.println("+-----------+-----------------------+-----------------------+-------------+----------+----------------+----------------------+------------+------------+");
        
        // Выводим общее количество строк (продуктов) в наборе.
        out.printf("%d строк в наборе\n", products.size());
    }

    // Вспомогательный метод для обрезки строки, если её длина превышает указанное значение.
    // Если строка длиннее, то она укорачивается и в конце добавляется троеточие.
    private String truncateString(String str, int length) {
        if (str == null) return "";
        return str.length() > length ? str.substring(0, length - 3) + "..." : str;
    }
} 