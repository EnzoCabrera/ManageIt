package com.example.estoque.services;

import com.example.estoque.dtos.StockDto;
import com.example.estoque.entities.Stock;
import com.example.estoque.exceptions.AppException;
import com.example.estoque.mapper.StockMapper;
import com.example.estoque.repositories.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;




@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    @Autowired
    private StockMapper stockMapper;

    //GET logic
    public StockDto getByCodProduct(Long codProduto) {
        Stock stock = stockRepository.findById(codProduto)
                .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));

        if (Boolean.TRUE.equals(stock.getIsDeleted())) {
            throw new AppException("Product is disabled. If you want to see it, active it", HttpStatus.NOT_FOUND);
        }

        return stockMapper.toDto(stock);
    }

    //POST logic
    public StockDto registerProduct(StockDto dto) {
        Stock stock = new Stock();
        stock.setCodProduto(dto.getCodProduto());
        stock.setProduct_name(dto.getProduct_name());
        stock.setPrice_in_cents(dto.getPrice_in_cents());
        stock.setQuantity(dto.getQuantity());

        if (stockRepository.existsById(dto.getCodProduto())) {
            throw new AppException("Produto já existe", HttpStatus.CONFLICT);
        }
        Stock save = stockRepository.save(stock);
        return stockMapper.toDto(save);
    }

    //PUT logic
    public StockDto updateProduct(Long codProduto, StockDto stockDto) {
        Stock stock = stockRepository.findById(codProduto)
                .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));

        stock.setProduct_name(stockDto.getProduct_name());
        stock.setQuantity(stockDto.getQuantity());
        stock.setPrice_in_cents(stockDto.getPrice_in_cents());

        Stock updatedStock = stockRepository.save(stock);
        return stockMapper.toDto(updatedStock);
    }

    //DELETE logic
    public StockDto deleteProduct(Long codProduto) {
        Stock stock = stockRepository.findByCodProdutoAndIsDeletedFalse(codProduto)
                .orElseThrow(() -> new AppException("Product not found or already disabled", HttpStatus.NOT_FOUND));

        stock.setIsDeleted(true);
        Stock updatedStock = stockRepository.save(stock);
        return stockMapper.toDto(updatedStock);
    }


}
