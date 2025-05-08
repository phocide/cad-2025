package ru.bsuedu.cad.lab.app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bsuedu.cad.lab.config.AppConfig;
import ru.bsuedu.cad.lab.service.OrderService;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.*;

import java.time.LocalDateTime;
import java.util.List;

public class OrderAppClient {
    public static void runDemo() {
        System.out.println("\n=== Лабораторная работа: Демонстрация JPA + Spring Data ===\n");
        try (var ctx = new AnnotationConfigApplicationContext(AppConfig.class)) {
            OrderService orderService = ctx.getBean(OrderService.class);
            CategoryRepository categoryRepository = ctx.getBean(CategoryRepository.class);
            ProductRepository productRepository = ctx.getBean(ProductRepository.class);
            CustomerRepository customerRepository = ctx.getBean(CustomerRepository.class);

            // Создаём и сохраняем связанные сущности
            Customer customer = new Customer();
            customer.setName("Иван Иванов");
            customer.setEmail("ivan@example.com");
            customer.setPhone("+375291234567");
            customer.setAddress("г. Минск, ул. Ленина, 1");
            customer = customerRepository.save(customer);

            Category category = new Category();
            category.setName("Корма");
            category.setDescription("Корма для животных");
            category = categoryRepository.save(category);

            Product product = new Product();
            product.setName("Корм для собак");
            product.setDescription("Сухой корм, 10 кг");
            product.setCategory(category);
            product.setPrice(99.99);
            product.setStockQuantity(10);
            product.setImageUrl("http://example.com/dogfood.jpg");
            product.setCreatedAt(LocalDateTime.now());
            product.setUpdatedAt(LocalDateTime.now());
            product = productRepository.save(product);

            // Создаём заказ и детали
            Order order = new Order();
            order.setCustomer(customer);
            order.setOrderDate(LocalDateTime.now());
            order.setTotalPrice(product.getPrice());
            order.setStatus("NEW");
            order.setShippingAddress(customer.getAddress());

            OrderDetail detail = new OrderDetail();
            detail.setProduct(product);
            detail.setQuantity(1);
            detail.setPrice(product.getPrice());

            Order savedOrder = orderService.createOrder(order, List.of(detail));

            System.out.println("\nСоздан заказ: ID=" + savedOrder.getOrderId() + ", клиент: " + savedOrder.getCustomer().getName());
            System.out.println("Количество заказов в БД: " + orderService.getAllOrders().size());

            // Проверка сохранения заказа
            Order найденный = ctx.getBean(OrderRepository.class).findById(savedOrder.getOrderId()).orElse(null);
            if (найденный != null) {
                System.out.println("Проверка: заказ с ID=" + найденный.getOrderId() + " найден в базе данных!");
                System.out.println("Клиент: " + найденный.getCustomer().getName() + ", сумма: " + найденный.getTotalPrice());
            } else {
                System.out.println("Ошибка: заказ не найден в базе!");
            }

            System.out.println("\n=== Конец демонстрации ===\n");
        }
    }
} 