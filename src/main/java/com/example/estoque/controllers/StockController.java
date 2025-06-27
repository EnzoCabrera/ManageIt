package com.example.estoque.controllers;

import com.example.estoque.dtos.StockRequestDto;
import com.example.estoque.dtos.StockResponseDto;
import com.example.estoque.services.StockService;
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

    @GetMapping("see/{codProd}")
    public ResponseEntity<StockResponseDto> getProductById(@PathVariable Long codProd) {
        StockResponseDto dto = stockService.getByCodProduct(codProd);
        return ResponseEntity.ok(dto);
    }


    @PostMapping("/registry")
    public ResponseEntity<StockResponseDto> registerProduct(@RequestBody StockRequestDto dto) {
        StockResponseDto created = stockService.registerProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @PutMapping("update/{codProd}")
    public ResponseEntity<StockResponseDto> updateProduct(@PathVariable Long codProd, @RequestBody StockRequestDto dto) {
        StockResponseDto updated = stockService.updateProduct(codProd, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("delete/{codProd}")
    public ResponseEntity<StockResponseDto> deleteProduct(@PathVariable Long codProd) {
        StockResponseDto deleted = stockService.deleteProduct(codProd);
        return ResponseEntity.ok(deleted);
    }
}
