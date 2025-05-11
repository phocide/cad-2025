package ru.bsuedu.cad.lab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.*;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        OrderDetailRepository orderDetailRepository,
                        ProductRepository productRepository,
                        CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Order createOrder(Order order, List<OrderDetail> details) {
        // Сохраняем заказ
        Order savedOrder = orderRepository.save(order);
        // Связываем детали с заказом и сохраняем
        for (OrderDetail detail : details) {
            detail.setOrder(savedOrder);
            orderDetailRepository.save(detail);
        }
        System.out.println("Заказ успешно создан: ID=" + savedOrder.getOrderId() + ", всего позиций: " + details.size());
        return savedOrder;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> findById(Integer id) {
        return orderRepository.findById(id);
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public boolean existsById(Integer id) {
        return orderRepository.existsById(id);
    }

    public void deleteById(Integer id) {
        orderRepository.deleteById(id);
    }
} 