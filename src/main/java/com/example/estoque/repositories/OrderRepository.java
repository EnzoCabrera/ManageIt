package com.example.estoque.repositories;

import com.example.estoque.entities.OrderEntities.Order;
import com.example.estoque.entities.OrderEntities.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    // Custom query to confirm that the order is not deleted
    Optional<Order> findBycodordAndIsDeletedFalse(Long codord);

    // Custom query to find orders that already overdue
    List<Order> findByOrdstsAndOrdpaydueBeforeAndIsDeletedFalse(OrderStatus ordsts, LocalDate date);

}
