package ru.bsuedu.cad.lab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderDetailRepository orderDetailRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private List<OrderDetail> details;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setOrderId(1);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(100.0);
        order.setStatus("NEW");
        order.setShippingAddress("Test Address");
        details = new ArrayList<>();
        OrderDetail detail = new OrderDetail();
        detail.setOrder(order);
        detail.setQuantity(2);
        detail.setPrice(50.0);
        details.add(detail);
    }

    @Test
    void createOrder_success() {
        // Arrange
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderDetailRepository.save(any(OrderDetail.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order savedOrder = orderService.createOrder(order, details);

        // Assert
        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getOrderId()).isEqualTo(1);
        verify(orderRepository, times(1)).save(order);
        verify(orderDetailRepository, times(details.size())).save(any(OrderDetail.class));
    }

    @Test
    void createOrder_fail_whenOrderRepositoryThrows() {
        // Arrange
        when(orderRepository.save(any(Order.class))).thenThrow(new RuntimeException("DB error"));

        // Act & Assert
        assertThatThrownBy(() -> orderService.createOrder(order, details))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("DB error");
        verify(orderRepository, times(1)).save(order);
        verify(orderDetailRepository, never()).save(any(OrderDetail.class));
    }
} 