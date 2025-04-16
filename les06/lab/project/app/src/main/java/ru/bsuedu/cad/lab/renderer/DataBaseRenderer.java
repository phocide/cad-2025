package ru.bsuedu.cad.lab.renderer;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.bsuedu.cad.lab.model.Product;
import ru.bsuedu.cad.lab.model.Category;
import ru.bsuedu.cad.lab.provider.ProductProvider;
import ru.bsuedu.cad.lab.provider.CategoryProvider;
import javax.sql.DataSource;
import java.util.List;

public class DataBaseRenderer implements Renderer {
    private final ProductProvider productProvider;
    private final CategoryProvider categoryProvider;
    private final JdbcTemplate jdbcTemplate;

    public DataBaseRenderer(ProductProvider productProvider, CategoryProvider categoryProvider, DataSource dataSource) {
        this.productProvider = productProvider;
        this.categoryProvider = categoryProvider;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void render() {
        // Сохраняем категории
        List<Category> categories = categoryProvider.getCategories();
        for (Category category : categories) {
            jdbcTemplate.update(
                "MERGE INTO CATEGORIES (category_id, name, description) KEY(category_id) VALUES (?, ?, ?)",
                category.getCategoryId(), category.getName(), category.getDescription()
            );
        }
        // Сохраняем продукты
        List<Product> products = productProvider.getProducts();
        for (Product product : products) {
            jdbcTemplate.update(
                "MERGE INTO PRODUCTS (product_id, name, description, category_id, price, stock_quantity, image_url, created_at, updated_at) KEY(product_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                product.getProductId(), product.getName(), product.getDescription(), product.getCategoryId(), product.getPrice(), product.getStockQuantity(), product.getImageUrl(), product.getCreatedAt(), product.getUpdatedAt()
            );
        }
        System.out.println("Данные из CSV успешно сохранены в базе данных.");

        // Проверочный вывод содержимого таблиц
        System.out.println("\nСодержимое таблицы CATEGORIES:");
        jdbcTemplate.query("SELECT * FROM CATEGORIES", rs -> {
            System.out.println("Категория: " + rs.getInt("category_id") + ", " + rs.getString("name") + ", " + rs.getString("description"));
        });
        System.out.println("\nСодержимое таблицы PRODUCTS:");
        jdbcTemplate.query("SELECT * FROM PRODUCTS", rs -> {
            System.out.println("Продукт: " + rs.getInt("product_id") + ", " + rs.getString("name") + ", " + rs.getString("description"));
        });
    }
} 