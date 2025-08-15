package com.example.estoque.services.StockServices;

import com.example.estoque.dtos.authDtos.PageResponseDto;
import com.example.estoque.dtos.stockDtos.StockResponseDto;
import com.example.estoque.entities.stockEntities.Stock;
import com.example.estoque.entities.stockEntities.StockSpecification;
import com.example.estoque.exceptions.AppException;
import com.example.estoque.mapper.StockMapper;
import com.example.estoque.repositories.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StockQueryService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    //GET products logic
    public PageResponseDto<StockResponseDto> getStockSlim(
            String productName,
            Long codprod,
            Pageable pageable
    ) {
        Specification<Stock> spec = Specification.where(StockSpecification.isNotDeleted())
                .and(StockSpecification.hasName(productName))
                .and(StockSpecification.hasCodprod(codprod));

        Page<Stock> page = stockRepository.findAll(spec, pageable);

        List<StockResponseDto> content = page.map(stockMapper::toDto).getContent();

        if (page.isEmpty()) {
            throw new AppException("No users found matching the given filters.", HttpStatus.NOT_FOUND);
        }

        return new PageResponseDto<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}
