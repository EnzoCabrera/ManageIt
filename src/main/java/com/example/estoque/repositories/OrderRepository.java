package com.example.estoque.repositories;

import com.example.estoque.entities.OrderEntities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    // Custom query to confirm that the order is not deleted
    Optional<Order> findBycodordAndIsDeletedFalse(Long codord);


}
