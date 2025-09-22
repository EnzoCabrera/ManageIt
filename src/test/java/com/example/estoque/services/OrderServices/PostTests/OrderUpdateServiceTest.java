package com.example.estoque.services.OrderServices.PostTests;

import com.example.estoque.dtos.itemDtos.ItemRequestDto;
import com.example.estoque.dtos.orderDtos.OrderRequestDto;
import com.example.estoque.dtos.orderDtos.OrderResponseDto;
import com.example.estoque.entities.ItemEntities.Item;
import com.example.estoque.entities.OrderEntities.Order;
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
import com.example.estoque.services.OrderServices.OrderNotificationService;
import com.example.estoque.services.OrderServices.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.estoque.entities.OrderEntities.OrderPaymentType.CREDIT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderUpdateServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private StockRepository stockRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private OrderNotificationService orderNotificationService;
    @Mock
    private AuditLogService auditLogService;
    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    //Customer not found test
    @Test
    void shouldThrowExceptionWhenCustomerNotFoundOnUpdate() {
        Long orderId = 999L;

        Order existingOrder = new Order();
        existingOrder.setCodord(orderId);

        Customer existingCustomer = new Customer();
        existingCustomer.setCodcus(123L);
        existingOrder.setCodcus(existingCustomer);

        OrderRequestDto dto = new OrderRequestDto();
        dto.setCodcus(1L);
        dto.setOrdpaytype(CREDIT);
        dto.setOrdsts(OrderStatus.PAID);
        dto.setOrdpaydue(LocalDate.now().plusDays(2));
        dto.setItems((new ArrayList<>()));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        when(customerRepository.findBycodcusAndIsDeletedFalse(1L))
                .thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> orderService.updateOrder(orderId, dto));
        assertEquals("Customer not found or disabled", ex.getMessage());
    }

    //Order not found test
    @Test
    void shouldThrowExceptionWhenOrderNotFoundOnUpdate() {
        Long orderId = 999L;

        Order existingOrder = new Order();
        existingOrder.setCodord(1L);

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        OrderRequestDto dto = new OrderRequestDto();
        dto.setCodcus(1L);
        dto.setOrdpaytype(CREDIT);
        dto.setOrdsts(OrderStatus.PAID);
        dto.setOrdpaydue(LocalDate.now().plusDays(2));
        dto.setItems((new ArrayList<>()));

        AppException ex = assertThrows(AppException.class, () -> orderService.updateOrder(orderId, dto));
        assertEquals("Order not found or deleted.",  ex.getMessage());
    }

    //Product not found test
    @Test
    void shouldThrowExceptionWhenProductNotFoundOnUpdate() {
        Long orderId = 999L;
        Long ProductId = 99L;

        Order existingOrder = new Order();
        existingOrder.setCodord(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        Customer cus = new Customer();
        cus.setCodcus(1L);
        existingOrder.setCodcus(cus);

        when(customerRepository.findBycodcusAndIsDeletedFalse(1L))
                .thenReturn(Optional.of(cus));

        Stock stock = new Stock();
        stock.setCodProd(ProductId);

        OrderRequestDto dto = new OrderRequestDto();
        dto.setCodcus(1L);
        dto.setOrdpaytype(CREDIT);
        dto.setOrdsts(OrderStatus.PAID);
        dto.setOrdpaydue(LocalDate.now().plusDays(2));
        dto.setItems(List.of(new ItemRequestDto(12L, 1, null)));

        when(stockRepository.findById(12L)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> orderService.updateOrder(orderId, dto));
        assertEquals("Product not found", ex.getMessage());
    }
}
