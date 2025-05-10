package ru.bsuedu.cad.lab.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.entity.Category;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CategoryRepository;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DataInitializer {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;

    @PostConstruct
    public void init() {
        importCategories();
        importCustomers();
        importProducts();
    }

    private void importCategories() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("category.csv").getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] parts = line.split(",", 3);
                if (parts.length == 3) {
                    Category c = new Category();
                    c.setCategoryId(Integer.parseInt(parts[0]));
                    c.setName(parts[1]);
                    c.setDescription(parts[2]);
                    categoryRepository.save(c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void importCustomers() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("customer.csv").getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] parts = line.split(",", 5);
                if (parts.length == 5) {
                    Customer c = new Customer();
                    c.setCustomerId(Integer.parseInt(parts[0]));
                    c.setName(parts[1]);
                    c.setEmail(parts[2]);
                    c.setPhone(parts[3]);
                    c.setAddress(parts[4]);
                    customerRepository.save(c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void importProducts() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("product.csv").getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean first = true;
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            while ((line = reader.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] parts = line.split(",", 9);
                if (parts.length == 9) {
                    Product p = new Product();
                    p.setProductId(Integer.parseInt(parts[0]));
                    p.setName(parts[1]);
                    p.setDescription(parts[2]);
                    int categoryId = Integer.parseInt(parts[3]);
                    Category cat = categoryRepository.findById(categoryId).orElse(null);
                    p.setCategory(cat);
                    p.setPrice(Double.parseDouble(parts[4]));
                    p.setStockQuantity(Integer.parseInt(parts[5]));
                    p.setImageUrl(parts[6]);
                    p.setCreatedAt(LocalDate.parse(parts[7], dtf).atStartOfDay());
                    p.setUpdatedAt(LocalDate.parse(parts[8], dtf).atStartOfDay());
                    productRepository.save(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 