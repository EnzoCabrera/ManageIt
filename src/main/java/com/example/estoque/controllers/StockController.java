package com.example.estoque.controllers;

import com.example.estoque.dtos.stockDtos.StockRequestDto;
import com.example.estoque.dtos.stockDtos.StockResponseDto;
import com.example.estoque.services.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock/product")
public class StockController {

    private final StockService stockService;

    public  StockController(StockService stockService) {
        this.stockService = stockService;
    }

    // GET all products
    @GetMapping("see")
    public ResponseEntity<List<StockResponseDto>> getAllStock(){
        return ResponseEntity.ok(stockService.getAllStock());
    }

    // GET product by ID
    @GetMapping("see/{codProd}")
    public ResponseEntity<StockResponseDto> getProductById(@PathVariable Long codProd) {
        StockResponseDto dto = stockService.getByCodProduct(codProd);
        return ResponseEntity.ok(dto);
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
