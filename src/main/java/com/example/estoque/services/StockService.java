package com.example.estoque.services;

import com.example.estoque.dtos.StockRequestDto;
import com.example.estoque.dtos.StockResponseDto;
import com.example.estoque.entities.Stock;
import com.example.estoque.exceptions.AppException;
import com.example.estoque.mapper.StockMapper;
import com.example.estoque.repositories.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    @Autowired
    private StockMapper stockMapper;

    //GET all products logic
    public List<StockResponseDto> getAllStock() {
        return stockRepository.findByIsDeletedFalse()
                .stream()
                .map(stockMapper::toDto)
                .toList();
    }

    // GET product by ID logic
    public StockResponseDto getByCodProduct(Long codProd) {
        Stock stock = stockRepository.findById(codProd)
                .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));

        if (Boolean.TRUE.equals(stock.getIsDeleted())) {
            throw new AppException("Product is disabled. If you want to see it, active it", HttpStatus.NOT_FOUND);
        }

        return stockMapper.toDto(stock);
    }

    //POST product logic
    public StockResponseDto registerProduct(StockRequestDto dto) {
        Stock stock = new Stock();
        stock.setProductName(dto.getProductName());
        stock.setPriceInCents(dto.getPriceInCents());
        stock.setQuantity(dto.getQuantity());

        Stock save = stockRepository.save(stock);
        return stockMapper.toDto(save);
    }

    //PUT product logic
    public StockResponseDto updateProduct(Long codProd, StockRequestDto stockRequestDto) {
        Stock stock = stockRepository.findById(codProd)
                .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));

        stock.setProductName(stockRequestDto.getProductName());
        stock.setQuantity(stockRequestDto.getQuantity());
        stock.setPriceInCents(stockRequestDto.getPriceInCents());

        Stock updatedStock = stockRepository.save(stock);
        return stockMapper.toDto(updatedStock);
    }

    //DELETE product logic
    public StockResponseDto deleteProduct(Long codProd) {
        Stock stock = stockRepository.findByCodProdAndIsDeletedFalse(codProd)
                .orElseThrow(() -> new AppException("Product not found or already disabled", HttpStatus.NOT_FOUND));

        stock.setIsDeleted(true);
        Stock updatedStock = stockRepository.save(stock);
        return stockMapper.toDto(updatedStock);
    }


}
