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

    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private EmailService emailService;

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
        stock.setUntype(dto.getUntype());
        stock.setUnqtt(dto.getUnqtt());
        stock.setMinimumQtd(dto.getMinimumQtd());

        Stock save = stockRepository.save(stock);



        // Log the creation of the stock item
        auditLogService.log(
                "Stock",
                save.getCodProd(),
                "CREATE",
                null,
                null,
                null,
                save.getCreatedBy()
        );

        return stockMapper.toDto(save);
    }

    //PUT product logic
    public StockResponseDto updateProduct(Long codProd, StockRequestDto stockRequestDto) {
        Stock stock = stockRepository.findById(codProd)
                .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));

        // Capture old values for audit logging
        String oldProductName = stock.getProductName();
        Integer oldQuantity = stock.getQuantity();
        Integer oldUnpricInCents = stock.getUnpricInCents();

        stock.setProductName(stockRequestDto.getProductName());
        stock.setQuantity(stockRequestDto.getQuantity());
        stock.setUnpricInCents(stockRequestDto.getUnpricInCents());
        stock.setMinimumQtd(stockRequestDto.getMinimumQtd());

        Stock updatedStock = stockRepository.save(stock);

        String actor = updatedStock.getUpdatedBy();

        //Log the update of the stock item

        //Product Name
        if (!oldProductName.equals(updatedStock.getProductName())) {
            auditLogService.log("Stock", updatedStock.getCodProd(), "UPDATE",
                    "productName", oldProductName, updatedStock.getProductName(), actor);
        }

        //Quantity
        if (!oldQuantity.equals(updatedStock.getQuantity())) {
            auditLogService.log("Stock", updatedStock.getCodProd(), "UPDATE",
                    "quantity", oldQuantity.toString(), updatedStock.getQuantity().toString(), actor);
        }

        //Unit Price in Cents
        if (!oldUnpricInCents.equals(updatedStock.getUnpricInCents())) {
            auditLogService.log("Stock", updatedStock.getCodProd(), "UPDATE",
                    "unpricInCents", oldUnpricInCents.toString(), updatedStock.getUnpricInCents().toString(), actor);
        }

        return stockMapper.toDto(updatedStock);
    }

    //DELETE product logic
    public StockResponseDto deleteProduct(Long codProd) {
        Stock stock = stockRepository.findByCodProdAndIsDeletedFalse(codProd)
                .orElseThrow(() -> new AppException("Product not found or already disabled", HttpStatus.NOT_FOUND));

        stock.setIsDeleted(true);
        Stock updatedStock = stockRepository.save(stock);

        // Log the deletion of the stock item
        auditLogService.log(
                "Stock",
                updatedStock.getCodProd(),
                "DELETE",
                null,
                null,
                null,
                updatedStock.getUpdatedBy()
        );

        return stockMapper.toDto(updatedStock);
    }


}
