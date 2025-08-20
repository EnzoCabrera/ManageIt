package com.example.estoque.controllers;

import com.example.estoque.config.Pageable.AllowedSort;
import com.example.estoque.dtos.authDtos.PageResponseDto;
import com.example.estoque.dtos.stockDtos.StockRequestDto;
import com.example.estoque.dtos.stockDtos.StockResponseDto;
import com.example.estoque.services.StockServices.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/stock/product")
public class StockController {

    private final StockService stockService;

    public  StockController(StockService stockService) {
        this.stockService = stockService;
    }

    // GET all products
    @GetMapping("/see")
    public ResponseEntity<PageResponseDto<StockResponseDto>> getStocks(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) Long codprod,
            @AllowedSort(value = {"codprod", "productName", "createdAt", "updatedAt"},
                        defaultProp = "createdAt", defaultDir = "DESC")
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(stockService.getStockSlim(productName, codprod, pageable));
    }

    // POST product logic
    @PostMapping("/registry")
    public ResponseEntity<StockResponseDto> registerProduct(@RequestBody StockRequestDto dto) {
        StockResponseDto created = stockService.registerProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT product logic
    @PutMapping("update/{codProd}")
    public ResponseEntity<StockResponseDto> updateProduct(@PathVariable Long codProd, @RequestBody StockRequestDto dto) {
        StockResponseDto updated = stockService.updateProduct(codProd, dto);
        return ResponseEntity.ok(updated);
    }

    // DELETE product logic
    @DeleteMapping("delete/{codProd}")
    public ResponseEntity<StockResponseDto> deleteProduct(@PathVariable Long codProd) {
        StockResponseDto deleted = stockService.deleteProduct(codProd);
        return ResponseEntity.ok(deleted);
    }
}
