package com.example.estoque.mapper;

import com.example.estoque.dtos.StockDto;
import com.example.estoque.entities.Stock;
import com.example.estoque.exceptions.AppException;
import com.example.estoque.repositories.StockRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class StockMapper {
    private final StockRepository stockRepository;

    public StockMapper(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public StockDto toDto(Stock stock) {
        StockDto dto = new StockDto();
        dto.setCod_produto(stock.getCod_produto());
        dto.setProduct_name(stock.getProduct_name());
        dto.setQuantity(stock.getQuantity());
        dto.setPrice_in_cents(stock.getPrice_in_cents());
        return dto;
    }
}
