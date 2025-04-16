package ru.bsuedu.cad.lab.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.util.List;

@Component
public class CategoryRequest {
    private static final Logger logger = LoggerFactory.getLogger(CategoryRequest.class);
    private final JdbcTemplate jdbcTemplate;

    public CategoryRequest(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void logCategoriesWithMultipleProducts() {
        String sql = "SELECT c.category_id, c.name, c.description, COUNT(p.product_id) as product_count " +
                "FROM CATEGORIES c " +
                "JOIN PRODUCTS p ON c.category_id = p.category_id " +
                "GROUP BY c.category_id, c.name, c.description " +
                "HAVING COUNT(p.product_id) > 1";
        List<String> results = jdbcTemplate.query(sql, (rs, rowNum) ->
                String.format("Категория: %d, %s, %s, товаров: %d",
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("product_count"))
        );
        if (results.isEmpty()) {
            logger.info("Нет категорий с количеством товаров больше единицы.");
        } else {
            logger.info("Категории с количеством товаров больше единицы:");
            for (String line : results) {
                logger.info(line);
            }
        }
    }
} 