package com.example.estoque.repositories;

import com.example.estoque.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StockRepository extends JpaRepository<Stock, Long> { }
