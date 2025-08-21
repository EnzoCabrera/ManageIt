package com.example.estoque.mapper;

import com.example.estoque.dtos.stockDtos.StockResponseDto;
import com.example.estoque.entities.stockEntities.Stock;
import com.example.estoque.repositories.StockRepository;
import org.springframework.stereotype.Component;

@Component
public class StockMapper {
    private final StockRepository stockRepository;

    public StockMapper(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public StockResponseDto toDto(Stock stock) {
        StockResponseDto dto = new StockResponseDto();
        dto.setCodProd(stock.getCodProd());
        dto.setProductName(stock.getProductName());
        dto.setQuantity(stock.getQuantity());
        dto.setUnpricInCents(stock.getUnpricInCents());
        dto.setUntype(stock.getUntype());
        dto.setUnqtt(stock.getUnqtt());
        dto.setMinimumQtd(stock.getMinimumQtd());
        dto.setIsDeleted(stock.getIsDeleted());
        return dto;
    }
}
