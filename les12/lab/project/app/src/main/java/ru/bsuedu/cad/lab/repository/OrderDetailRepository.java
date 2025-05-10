package ru.bsuedu.cad.lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM OrderDetail od WHERE od.order.orderId = :orderId")
    void deleteAllByOrderId(Integer orderId);
} 