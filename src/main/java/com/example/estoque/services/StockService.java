package com.example.estoque.services;

import com.example.estoque.dtos.stockDtos.StockRequestDto;
import com.example.estoque.dtos.stockDtos.StockResponseDto;
import com.example.estoque.entities.stockEntities.Stock;
import com.example.estoque.entities.stockEntities.StockSpecification;
import com.example.estoque.exceptions.AppException;
import com.example.estoque.mapper.StockMapper;
import com.example.estoque.repositories.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    @Autowired
    private StockMapper stockMapper;

    //GET products logic
    public List<StockResponseDto> getFilterStock(
            String productName,
            Long codprod
        ) {
        Specification<Stock> spec = Specification.where(StockSpecification.isNotDeleted())
                .and(StockSpecification.hasName(productName))
                .and(StockSpecification.hasCodprod(codprod));

        return stockRepository.findAll(spec)
                .stream()
                .map(stockMapper::toDto)
                .toList();
    }

    //POST product logic
    public StockResponseDto registerProduct(StockRequestDto dto) {
        Stock stock = new Stock();
        stock.setProductName(dto.getProductName());
        stock.setQuantity(dto.getQuantity());
        stock.setUnpricInCents(dto.getUnpricInCents());

        Stock save = stockRepository.save(stock);
        return stockMapper.toDto(save);
    }

    //PUT product logic
    public StockResponseDto updateProduct(Long codProd, StockRequestDto stockRequestDto) {
        Stock stock = stockRepository.findById(codProd)
                .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));

        stock.setProductName(stockRequestDto.getProductName());
        stock.setQuantity(stockRequestDto.getQuantity());
        stock.setUnpricInCents(stockRequestDto.getUnpricInCents());

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
