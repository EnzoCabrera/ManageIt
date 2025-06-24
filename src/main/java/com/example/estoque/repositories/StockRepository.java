package com.example.estoque.repositories;

import com.example.estoque.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByCodProdutoAndIsDeletedFalse(Long codProduto);
}
