package com.example.estoque.services.OrderServices.DeleteTests;

import com.example.estoque.dtos.orderDtos.OrderRequestDto;
import com.example.estoque.dtos.orderDtos.OrderResponseDto;
import com.example.estoque.entities.ItemEntities.Item;
import com.example.estoque.entities.OrderEntities.Order;
import com.example.estoque.entities.OrderEntities.OrderStatus;
import com.example.estoque.entities.customerEntities.Customer;
import com.example.estoque.entities.stockEntities.Stock;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderDeleteServiceTest {
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

    //Order not found test
    @Test
    void shouldThrowExceptionWHenOrderNotFoundOnDelete() {
        Long orderId = 999L;

        Order existingOrder = new Order();
        existingOrder.setCodord(1L);
        Customer cus = new Customer();
        cus.setCodcus(1L);
        existingOrder.setCodcus(cus);

        OrderRequestDto dto = new OrderRequestDto();
        dto.setCodcus(cus.getCodcus());
        dto.setOrdpaytype(CREDIT);
        dto.setOrdsts(OrderStatus.PAID);
        dto.setOrdpaydue(LocalDate.now().plusDays(2));
        dto.setItems((new ArrayList<>()));

        when(orderRepository.findBycodordAndIsDeletedFalse(orderId)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> orderService.deleteOrder(orderId));
        assertEquals("Order not found or deleted.",  ex.getMessage());
    }

    //Delete order and items successfully test
    @Test
    void shouldDeleteOrderAndItemsSuccessfully() {
        Long orderId = 999L;

        Stock stock = new Stock();
        stock.setCodProd(1L);

        Item item1 = new Item();
        item1.setIsDeleted(false);
        item1.setCodprod(stock);

        Item item2 = new Item();
        item2.setIsDeleted(false);
        item2.setCodprod(stock);

        Order order = new Order();
        order.setCodord(orderId);
        order.setIsDeleted(false);
        order.setItems(List.of(item1, item2));
        order.setUpdatedBy("test_user");

        when(orderRepository.findBycodordAndIsDeletedFalse(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponseDto dto = new OrderResponseDto();
        dto.setCodord(orderId);
        when(orderMapper.toDto(any(Order.class))).thenReturn(dto);

        OrderResponseDto result = orderService.deleteOrder(orderId);

        assertTrue(order.getIsDeleted());
        assertTrue(order.getItems().stream().allMatch(Item::getIsDeleted));
        assertEquals(orderId, result.getCodord());

        verify(orderRepository).save(order);
        verify(orderMapper).toDto(order);
        verify(auditLogService).log(
                eq("Order"),
                eq(orderId),
                eq("DELETE"),
                isNull(),
                isNull(),
                isNull(),
                eq("test_user"));
    }

    //Delete order successfully when there are no items test
    @Test
    void shouldDeleteOrderSuccessfullyWhenThereAreNoItems() {
        Long orderId = 999L;

        Order order = new Order();
        order.setCodord(orderId);
        order.setIsDeleted(false);
        order.setItems(null);
        order.setUpdatedBy("test_user");

        when(orderRepository.findBycodordAndIsDeletedFalse(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponseDto dto = new OrderResponseDto();
        dto.setCodord(orderId);
        when(orderMapper.toDto(any(Order.class))).thenReturn(dto);

        OrderResponseDto result = orderService.deleteOrder(orderId);

        assertTrue(order.getIsDeleted());
        assertTrue(order.getItems() == null || order.getItems().isEmpty());
        assertEquals(orderId, result.getCodord());

        verify(orderRepository).save(order);
        verify(orderMapper).toDto(order);
        verify(auditLogService).log(
                eq("Order"),
                eq(orderId),
                eq("DELETE"),
                isNull(),
                isNull(),
                isNull(),
                eq("test_user"));
    }
}
