package ru.bsuedu.cad.lab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import ru.bsuedu.cad.lab.repository.OrderDetailRepository;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Category;
import ru.bsuedu.cad.lab.repository.CategoryRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ru.bsuedu.cad.lab.config.TestConfig.class)
@Transactional
@Rollback
class OrderServiceIntegrationTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private Order order;
    private List<OrderDetail> details;

    @BeforeEach
    void setUp() {
        // Создаём и сохраняем тестовую категорию
        Category category = new Category();
        category.setName("Test Category");
        category.setDescription("Test Category Description");
        categoryRepository.save(category);

        // Создаём и сохраняем тестовый продукт
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(50.0);
        product.setCategory(category);
        product.setStockQuantity(10);
        productRepository.save(product);

        // Создаём и сохраняем тестового покупателя
        Customer customer = new Customer();
        customer.setName("Test Customer");
        customer.setEmail("test@example.com");
        customer.setPhone("1234567890");
        customer.setAddress("Test Address");
        customerRepository.save(customer);

        order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(100.0);
        order.setStatus("NEW");
        order.setShippingAddress("Test Address");
        order.setCustomer(customer);

        details = new ArrayList<>();
        OrderDetail detail = new OrderDetail();
        detail.setOrder(order);
        detail.setQuantity(2);
        detail.setPrice(50.0);
        detail.setProduct(product);
        details.add(detail);
    }

    @Test
    void createOrder_integration_success() {
        Order savedOrder = orderService.createOrder(order, details);
        assertThat(savedOrder.getOrderId()).isNotNull();
        assertThat(orderRepository.findById(savedOrder.getOrderId())).isPresent();
        List<OrderDetail> foundDetails = orderDetailRepository.findAll();
        assertThat(foundDetails).isNotEmpty();
    }

    @Test
    void createOrder_withNonExistentProduct_shouldFail() {
        // Создаём и сохраняем категорию
        Category category = new Category();
        category.setName("Test Category");
        category.setDescription("Test Category Description");
        categoryRepository.save(category);

        // Продукт НЕ сохраняем в БД!
        Product product = new Product();
        product.setName("Fake Product");
        product.setPrice(99.0);
        product.setCategory(category);
        product.setStockQuantity(5);

        // Создаём и сохраняем покупателя
        Customer customer = new Customer();
        customer.setName("Test Customer");
        customer.setEmail("test2@example.com");
        customer.setPhone("1234567890");
        customer.setAddress("Test Address");
        customerRepository.save(customer);

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(99.0);
        order.setStatus("NEW");
        order.setShippingAddress("Test Address");
        order.setCustomer(customer);

        List<OrderDetail> details = new ArrayList<>();
        OrderDetail detail = new OrderDetail();
        detail.setOrder(order);
        detail.setQuantity(1);
        detail.setPrice(99.0);
        detail.setProduct(product); // продукт не сохранён!
        details.add(detail);

        assertThatThrownBy(() -> orderService.createOrder(order, details))
            .isInstanceOf(Exception.class); // Можно уточнить тип, если нужно
    }
} 