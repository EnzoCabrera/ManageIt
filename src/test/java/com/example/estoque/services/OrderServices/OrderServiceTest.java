package com.example.estoque.services.OrderServices;

import com.example.estoque.dtos.itemDtos.ItemRequestDto;
import com.example.estoque.dtos.orderDtos.OrderRequestDto;
import com.example.estoque.entities.OrderEntities.OrderStatus;
import com.example.estoque.entities.customerEntities.Customer;
import com.example.estoque.entities.stockEntities.Stock;
import com.example.estoque.entities.stockEntities.StockUnitType;
import com.example.estoque.exceptions.AppException;
import com.example.estoque.mapper.OrderMapper;
import com.example.estoque.repositories.CustomerRepositories.CustomerRepository;
import com.example.estoque.repositories.ItemRepository;
import com.example.estoque.repositories.OrderRepositories.OrderRepository;
import com.example.estoque.repositories.StockRepository;
import com.example.estoque.services.AuditLogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {



    @Mock private CustomerRepository customerRepository;
    @Mock private StockRepository stockRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private ItemRepository itemRepository;
    @Mock private OrderNotificationService orderNotificationService;
    @Mock private AuditLogService auditLogService;
    @Mock private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;


    //Customer not found test
    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {
        OrderRequestDto dto = new OrderRequestDto();
        dto.setCodcus(1L);
        dto.setItems(new ArrayList<>());

        when(customerRepository.findBycodcusAndIsDeletedFalse(1L))
                .thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> orderService.registerOrder(dto));
        assertEquals("Customer not found or disabled", ex.getMessage());
    }

    // Product not found test
    @Test
    void shouldThrowExceptionWhenProductNotFound() {

        Long customerId = 1L;
        Long invalidProductId = 99L;

        Customer cus = new Customer();
        cus.setCodcus(customerId);


        OrderRequestDto dto = new OrderRequestDto();
        dto.setCodcus(customerId);
        dto.setOrdsts(OrderStatus.PAID);
        dto.setOrdpaydue(LocalDate.now());
        dto.setItems(List.of(new ItemRequestDto(invalidProductId, 1, null)));

        when(customerRepository.findBycodcusAndIsDeletedFalse(customerId))
                .thenReturn(Optional.of(cus));

        when(stockRepository.findById(invalidProductId))
                .thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> orderService.registerOrder(dto));
        assertEquals("Product not found", ex.getMessage());
    }

    // Product unit price not found test
    @Test
    void shouldThrowExceptionWhenProductUnitPriceNotFound() {

        Long customerId = 1L;
        Long productId = 10L;

        Customer cus = new Customer();
        cus.setCodcus(customerId);

        Stock stock = new Stock();
        stock.setCodProd(productId);
        stock.setUnpricInCents(0);
        stock.setQuantity(10);
        stock.setMinimumQtd(2);
        stock.setUntype(StockUnitType.UNIT);
        stock.setUnqtt(1);

        ItemRequestDto itemDto = new ItemRequestDto();
        itemDto.setCodprod(productId);
        itemDto.setQuantity(1);
        itemDto.setDiscountPercent(null);

        OrderRequestDto dto = new OrderRequestDto();
        dto.setCodcus(customerId);
        dto.setOrdsts(OrderStatus.PAID);
        dto.setOrdpaydue(LocalDate.now());
        dto.setItems(List.of((itemDto)));

        when (customerRepository.findBycodcusAndIsDeletedFalse(customerId))
                .thenReturn(Optional.of(cus));

        when(stockRepository.findById(productId))
                .thenReturn(Optional.of(stock));

        AppException ex = assertThrows(AppException.class, () -> orderService.registerOrder(dto));
        assertEquals("Product should have a unit price", ex.getMessage());
    }
}
