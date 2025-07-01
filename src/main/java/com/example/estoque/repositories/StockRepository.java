package com.example.estoque.repositories;

import com.example.estoque.entities.stockEntities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;


public interface StockRepository extends JpaRepository<Stock, Long>, JpaSpecificationExecutor<Stock> {

    // Custom query to confirm that the product is not deleted
    Optional<Stock> findByCodProdAndIsDeletedFalse(Long codProd);

}
