package com.example.estoque.services.StockServices;

import com.example.estoque.dtos.authDtos.PageResponseDto;
import com.example.estoque.dtos.stockDtos.StockRequestDto;
import com.example.estoque.dtos.stockDtos.StockResponseDto;
import com.example.estoque.entities.stockEntities.Stock;
import com.example.estoque.entities.stockEntities.StockSpecification;
import com.example.estoque.exceptions.AppException;
import com.example.estoque.mapper.StockMapper;
import com.example.estoque.repositories.StockRepository;
import com.example.estoque.services.AuditLogService;
import com.example.estoque.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        Integer oldMinimumQtd = stock.getMinimumQtd();
        String oldUntype = stock.getUntype().toString();
        Integer oldUnqtt = stock.getUnqtt();
        Boolean oldIsDeleted = stock.getIsDeleted();


        stock.setProductName(stockRequestDto.getProductName());
        stock.setQuantity(stockRequestDto.getQuantity());
        stock.setUnpricInCents(stockRequestDto.getUnpricInCents());
        stock.setMinimumQtd(stockRequestDto.getMinimumQtd());
        stock.setUntype(stockRequestDto.getUntype());
        stock.setUnqtt(stockRequestDto.getUnqtt());
        stock.setIsDeleted(stockRequestDto.getIsDeleted());

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

        //Minimum Quantity
        if (!oldMinimumQtd.equals(updatedStock.getMinimumQtd())) {
            auditLogService.log("Stock", updatedStock.getCodProd(), "UPDATE",
                    "minimumQtd", oldMinimumQtd.toString(), updatedStock.getMinimumQtd().toString(), actor);
        }

        //Unit Type
        if (!oldUntype.equals(updatedStock.getUntype().toString())) {
            auditLogService.log("Stock", updatedStock.getCodProd(), "UPDATE",
                    "untype", oldUntype, updatedStock.getUntype().toString(), actor);
        }

        //Unit Quantity
        if (!oldUnqtt.equals(updatedStock.getUnqtt())) {
            auditLogService.log("Stock", updatedStock.getCodProd(), "UPDATE",
                    "unqtt", oldUnqtt.toString(), updatedStock.getUnqtt().toString(), actor);
        }

        //Is Deleted
        if (!oldIsDeleted.equals(updatedStock.getIsDeleted())) {
            auditLogService.log("Stock", updatedStock.getCodProd(), "UPDATE",
                    "isDeleted", oldIsDeleted.toString(), updatedStock.getIsDeleted().toString(), actor);
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
