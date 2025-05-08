package ru.bsuedu.cad.lab.renderer;

import ru.bsuedu.cad.lab.model.Product;
import ru.bsuedu.cad.lab.provider.ProductProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HTMLTableRenderer implements Renderer {
    // Компонент для получения списка продуктов.
    private final ProductProvider productProvider;
    // Используем форматтер только для даты
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // Название файла HTML
    private static final String HTML_FILE_NAME = "products.html";

    public HTMLTableRenderer(ProductProvider productProvider) {
        this.productProvider = productProvider;
    }

    @Override
    public void render() {
        // Получаем список продуктов через ProductProvider.
        List<Product> products = productProvider.getProducts();
        
        // Создаем HTML-строку с таблицей продуктов
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"ru\">\n");
        html.append("<head>\n");
        html.append("    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <title>Продукты зоомагазина</title>\n");
        html.append("    <style>\n");
        html.append("        table { border-collapse: collapse; width: 100%; }\n");
        html.append("        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n");
        html.append("        th { background-color: #f2f2f2; }\n");
        html.append("        tr:nth-child(even) { background-color: #f9f9f9; }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <h1>Продукты зоомагазина</h1>\n");
        html.append("    <table>\n");
        html.append("        <thead>\n");
        html.append("            <tr>\n");
        html.append("                <th>ID</th>\n");
        html.append("                <th>Название</th>\n");
        html.append("                <th>Описание</th>\n");
        html.append("                <th>Категория ID</th>\n");
        html.append("                <th>Цена</th>\n");
        html.append("                <th>Кол-во на складе</th>\n");
        html.append("                <th>URL изображения</th>\n");
        html.append("                <th>Создано</th>\n");
        html.append("                <th>Обновлено</th>\n");
        html.append("            </tr>\n");
        html.append("        </thead>\n");
        html.append("        <tbody>\n");
        
        // Создаем строки таблицы для каждого продукта
        for (Product product : products) {
            html.append("            <tr>\n");
            html.append("                <td>").append(product.getProductId()).append("</td>\n");
            html.append("                <td>").append(product.getName()).append("</td>\n");
            html.append("                <td>").append(product.getDescription()).append("</td>\n");
            html.append("                <td>").append(product.getCategoryId()).append("</td>\n");
            html.append("                <td>").append(String.format("%.2f", product.getPrice())).append("</td>\n");
            html.append("                <td>").append(product.getStockQuantity()).append("</td>\n");
            html.append("                <td><a href=\"").append(product.getImageUrl()).append("\">").append(product.getImageUrl()).append("</a></td>\n");
            html.append("                <td>").append(dateFormatter.format(product.getCreatedAt().toLocalDate())).append("</td>\n");
            html.append("                <td>").append(dateFormatter.format(product.getUpdatedAt().toLocalDate())).append("</td>\n");
            html.append("            </tr>\n");
        }
        
        html.append("        </tbody>\n");
        html.append("    </table>\n");
        html.append("    <p>").append(products.size()).append(" продуктов найдено</p>\n");
        html.append("</body>\n");
        html.append("</html>");
        
        try {
            // Создаем директорию output, если она не существует
            Path outputDir = Paths.get("output");
            if (!Files.exists(outputDir)) {
                Files.createDirectory(outputDir);
            }
            
            String filePath = outputDir.toAbsolutePath() + File.separator + HTML_FILE_NAME;
            System.out.println("Попытка записать HTML-файл в: " + filePath);
            
            // Записываем HTML в файл с явным указанием UTF-8 кодировки и добавлением BOM
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                
                // Записываем HTML
                OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                writer.write(html.toString());
                writer.flush();
                System.out.println("HTML-таблица успешно создана в файле: " + new File(filePath).getAbsolutePath());
            } 
            
            // Также выводим информацию о продуктах в консоль для подтверждения
            System.out.println("Обработано " + products.size() + " продуктов");
        } catch (IOException e) {
            System.err.println("Ошибка при записи HTML файла: " + e.getMessage());
            e.printStackTrace(); // Добавляем вывод стека ошибки для отладки
        }
    }
} 