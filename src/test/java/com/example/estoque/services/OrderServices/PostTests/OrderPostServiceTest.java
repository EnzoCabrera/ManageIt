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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.estoque.entities.OrderEntities.OrderPaymentType.CREDIT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderPostServiceTest {
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

    //Invalid order payment date test
    @Test
    void ShouldThrowExceptionWhenOrderPaymentDateIsInvalid() {
        Long customerId = 1L;
        Long productId = 10L;

        Customer cus = new Customer();
        cus.setCodcus(customerId);

        Stock stock = new Stock();
        stock.setCodProd(productId);
        stock.setUnpricInCents(1000);
        stock.setQuantity(10);
        stock.setMinimumQtd(2);
        stock.setUntype(StockUnitType.UNIT);
        stock.setUnqtt(1);

        OrderRequestDto dto = new OrderRequestDto();
        dto.setCodcus(customerId);
        dto.setOrdsts(OrderStatus.PENDING);
        dto.setOrdpaydue(LocalDate.now().minusDays(1));
        dto.setItems(List.of(new ItemRequestDto(productId, 1, null)));

        when (customerRepository.findBycodcusAndIsDeletedFalse(customerId))
                .thenReturn(Optional.of(cus));

        when(stockRepository.findById(productId))
                .thenReturn(Optional.of(stock));

        AppException ex = assertThrows(AppException.class, () -> orderService.registerOrder(dto));
        assertEquals("Cannot create a pending order with a payment due date in the past. Use OVERDUE, PAID or CANCELLED instead.", ex.getMessage());
    }

    //Insufficient stock test
    @Test
    void ShouldThrowExceptionWhenInsufficientStock() {
        Long customerId = 1L;
        Long productId = 10L;

        Customer cus = new Customer();
        cus.setCodcus(customerId);

        Stock stock = new Stock();
        stock.setCodProd(productId);
        stock.setUnpricInCents(1000);
        stock.setQuantity(0);
        stock.setMinimumQtd(1);
        stock.setUntype(StockUnitType.UNIT);
        stock.setUnqtt(1);

        ItemRequestDto itemDto = new ItemRequestDto();
        itemDto.setCodprod(productId);
        itemDto.setQuantity(5);

        OrderRequestDto dto = new OrderRequestDto();
        dto.setCodcus(customerId);
        dto.setOrdsts(OrderStatus.PENDING);
        dto.setOrdpaydue(LocalDate.now());
        dto.setItems(List.of(itemDto));

        when (customerRepository.findBycodcusAndIsDeletedFalse(customerId))
                .thenReturn(Optional.of(cus));

        when(stockRepository.findById(productId))
                .thenReturn(Optional.of(stock));

        AppException ex = assertThrows(AppException.class, () -> orderService.registerOrder(dto));
        String expectedMessage = String.format(
                "Insufficient stock for product ID: %d. Available: %d, Requested: %d",
                stock.getCodProd(), stock.getQuantity(), itemDto.getQuantity()
        );
        assertEquals(expectedMessage, ex.getMessage());

    }

    //Notify when stock falls below minimum test
    @Test
    void ShouldThrowExceptionWhenStockFallsBelowMinimum() {
        Stock stock = new Stock();
        stock.setCodProd(1L);
        stock.setUnpricInCents(100);
        stock.setUnqtt(1);
        stock.setUntype(StockUnitType.UNIT);
        stock.setQuantity(6);
        stock.setMinimumQtd(5);

        ItemRequestDto itemDto = new ItemRequestDto();
        itemDto.setCodprod(1L);
        itemDto.setQuantity(2);

        OrderRequestDto dto = new OrderRequestDto();
        dto.setCodcus(1L);
        dto.setItems(List.of(itemDto));
        dto.setOrdsts(OrderStatus.PAID);
        dto.setOrdpaydue(LocalDate.now());

        Customer customer = new Customer();
        when(customerRepository.findBycodcusAndIsDeletedFalse(1L)).thenReturn(Optional.of(customer));
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderMapper.toDto(any(Order.class))).thenReturn(new OrderResponseDto());

        orderService.registerOrder(dto);

        verify(orderNotificationService).notifyLowStock(stock);
    }

    // Order with multiple items succeed test
    @Test
    void ShouldThrowExceptionWhenOrderWithMultipleItemsSucceed() {
        Long customerId = 1L;

        Customer cus = new Customer();
        cus.setCodcus(customerId);

        //Item 1
        Stock stock = new Stock();
        stock.setCodProd(1L);
        stock.setUnpricInCents(10000);
        stock.setUnqtt(1);
        stock.setUntype(StockUnitType.UNIT);
        stock.setQuantity(50);
        stock.setMinimumQtd(1);

        //Item 2
        Stock stock2 = new Stock();
        stock2.setCodProd(2L);
        stock2.setUnpricInCents(10000);
        stock2.setUnqtt(1);
        stock2.setUntype(StockUnitType.UNIT);
        stock2.setQuantity(50);
        stock2.setMinimumQtd(1);

        //Item 3
        Stock stock3 = new Stock();
        stock3.setCodProd(3L);
        stock3.setUnpricInCents(10000);
        stock3.setUnqtt(1);
        stock3.setUntype(StockUnitType.UNIT);
        stock3.setQuantity(50);
        stock3.setMinimumQtd(1);

        when (customerRepository.findBycodcusAndIsDeletedFalse(customerId))
                .thenReturn(Optional.of(cus));

        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        when(stockRepository.findById(2L)).thenReturn(Optional.of(stock2));
        when(stockRepository.findById(3L)).thenReturn(Optional.of(stock3));

        when(stockRepository.save(any(Stock.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            o.setCodord(999L);
            return o;
        });

        ItemRequestDto item1 = new ItemRequestDto();
        item1.setCodprod(1L);
        item1.setQuantity(10);
        item1.setDiscountPercent(0.0F);

        ItemRequestDto item2 = new ItemRequestDto();
        item2.setCodprod(2L);
        item2.setQuantity(10);
        item2.setDiscountPercent(0.0F);

        ItemRequestDto item3 = new ItemRequestDto();
        item3.setCodprod(3L);
        item3.setQuantity(10);
        item3.setDiscountPercent(10.0F);


        OrderRequestDto dto = new OrderRequestDto();
        dto.setCodcus(customerId);
        dto.setOrdpaytype(CREDIT);
        dto.setOrdsts(OrderStatus.PAID);
        dto.setOrdpaydue(LocalDate.now().plusDays(2));
        dto.setOrdnote("Pedido teste");
        dto.setOrdpaydue(LocalDate.now());
        dto.setItems(List.of(item1, item2, item3));


        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setCodord(999L);
        when(orderMapper.toDto(any(Order.class))).thenReturn(responseDto);

        OrderResponseDto result = orderService.registerOrder(dto);

        assertNotNull(result);
        assertEquals(999L, result.getCodord());

        verify(orderRepository).save(any(Order.class));
        verify(itemRepository, times(3)).save(any(Item.class));
        verify(orderMapper).toDto(any(Order.class));
    }

    // Order with discount test
    @Test
    void ShouldThrowExceptionWhenOrderHasDiscount() {
        Long customerId = 1L;
        int unitPrice = 1000;
        int quantity = 2;
        float discountPercent = 10.0F;

        OrderRequestDto dto = new OrderRequestDto();
        dto.setCodcus(customerId);
        dto.setOrdsts(OrderStatus.PAID);
        dto.setOrdpaydue(LocalDate.now().plusDays(1));

        ItemRequestDto item = new ItemRequestDto();
        item.setCodprod(10L);
        item.setQuantity(quantity);
        item.setDiscountPercent(discountPercent);
        dto.setItems(List.of(item));

        Customer cus = new Customer();
        cus.setCodcus(customerId);
        when (customerRepository.findBycodcusAndIsDeletedFalse(customerId))
                .thenReturn(Optional.of(cus));

        Stock stock = new Stock();
        stock.setCodProd(10L);
        stock.setUnpricInCents(unitPrice);
        stock.setUnqtt(1);
        stock.setUntype(StockUnitType.UNIT);
        stock.setQuantity(10);
        stock.setMinimumQtd(1);
        when(stockRepository.findById(10L)).thenReturn(Optional.of(stock));

        when(orderRepository.save(Mockito.any())).thenAnswer(invocation -> {
            Order saved = invocation.getArgument(0);
            saved.setCodord(999L);
            return saved;
        });

        when(itemRepository.save(Mockito.any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderMapper.toDto(Mockito.any())).thenReturn(new OrderResponseDto());

        OrderResponseDto response = orderService.registerOrder(dto);

        ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
        verify(itemRepository).save(itemCaptor.capture());

        Item savedItem = itemCaptor.getValue();
        int expectedCost = (int) Math.round(unitPrice * quantity * (1 - discountPercent / 100.0));
        assertEquals(expectedCost, savedItem.getTotalInCents());
    }
}
