package com.example.estoque.controllers;

import com.example.estoque.dtos.StockDto;
import com.example.estoque.entities.Stock;
import com.example.estoque.repositories.StockRepository;
import com.example.estoque.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("see/{codProduto}")
    public ResponseEntity<StockDto> getProductById(@PathVariable Long codProduto) {
        StockDto dto = stockService.getByCodProduct(codProduto);
        return ResponseEntity.ok(dto);
    }


    @PostMapping("/registry")
    public ResponseEntity<StockDto> registerProduct(@RequestBody StockDto dto) {
        StockDto created = stockService.registerProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @PutMapping("update/{codProduto}")
    public ResponseEntity<StockDto> updateProduct(@PathVariable Long codProduto, @RequestBody StockDto dto) {
        StockDto updated = stockService.updateProduct(codProduto, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("delete/{codProduto}")
    public ResponseEntity<StockDto> deleteProduct(@PathVariable Long codProduto) {
        StockDto deleted = stockService.deleteProduct(codProduto);
        return ResponseEntity.ok(deleted);
    }
}
