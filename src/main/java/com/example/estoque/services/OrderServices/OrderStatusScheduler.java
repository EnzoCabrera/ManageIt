package com.example.estoque.services.OrderServices;

import com.example.estoque.entities.OrderEntities.Order;
import com.example.estoque.entities.OrderEntities.OrderStatus;
import com.example.estoque.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderStatusScheduler {

    private final OrderRepository orderRepository;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void updateOverdueOrders() {
        LocalDate today = LocalDate.now();
        List<Order> overdueOrders = orderRepository.findByOrdstsAndOrdpaydueBeforeAndIsDeletedFalse(OrderStatus.PENDING, today);

        overdueOrders.forEach(order -> order.setOrdsts(OrderStatus.OVERDUE));

        orderRepository.saveAll(overdueOrders);
    }
}
