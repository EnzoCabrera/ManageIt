package com.example.estoque.services.OrderServices.PostTests;

import com.example.estoque.dtos.itemDtos.ItemRequestDto;
import com.example.estoque.dtos.orderDtos.OrderRequestDto;
import com.example.estoque.dtos.orderDtos.OrderResponseDto;
import com.example.estoque.entities.ItemEntities.Item;
import com.example.estoque.entities.OrderEntities.Order;
import com.example.estoque.entities.OrderEntities.OrderPaymentType;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    //Product unit price not found test
    @Test
    void shouldThrowExceptionWhenProductUnitPriceNotFoundOnUpdate() {
        Long orderId = 999L;
        Order existingOrder = new Order();
        existingOrder.setCodord(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        Long customerId = 1L;
        Customer cus = new Customer();
        cus.setCodcus(customerId);
        existingOrder.setCodcus(cus);

        when(customerRepository.findBycodcusAndIsDeletedFalse(1L))
                .thenReturn(Optional.of(cus));

        Long productId = 99L;

        Stock stock = new Stock();
        stock.setCodProd(productId);
        stock.setUnpricInCents(0);
        stock.setQuantity(10);
        stock.setMinimumQtd(2);
        stock.setUntype(StockUnitType.UNIT);
        stock.setUnqtt(1);

        when(stockRepository.findById(productId))
                .thenReturn(Optional.of(stock));

        ItemRequestDto itemDto = new ItemRequestDto();
        itemDto.setCodprod(productId);
        itemDto.setQuantity(1);
        itemDto.setDiscountPercent(null);

        OrderRequestDto dto = new OrderRequestDto();
        dto.setCodcus(customerId);
        dto.setOrdsts(OrderStatus.PAID);
        dto.setOrdpaydue(LocalDate.now());
        dto.setItems(List.of((itemDto)));

        AppException ex = assertThrows(AppException.class, () -> orderService.updateOrder(orderId ,dto));
        assertEquals("Product should have a unit price", ex.getMessage());
    }

    //Invalid order payment date test
    @Test
    void shouldThrowExceptionWhenOrderPaymentDateIsInvalidOnUpdate() {
        Long orderId = 999L;
        Order existingOrder = new Order();
        existingOrder.setCodord(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        Long customerId = 1L;
        Customer cus = new Customer();
        cus.setCodcus(customerId);
        existingOrder.setCodcus(cus);

        when(customerRepository.findBycodcusAndIsDeletedFalse(1L))
                .thenReturn(Optional.of(cus));

        Long productId = 99L;

        Stock stock = new Stock();
        stock.setCodProd(productId);
        stock.setUnpricInCents(1000);
        stock.setQuantity(10);
        stock.setMinimumQtd(2);
        stock.setUntype(StockUnitType.UNIT);
        stock.setUnqtt(1);

        when(stockRepository.findById(productId))
                .thenReturn(Optional.of(stock));

        OrderRequestDto dto = new OrderRequestDto();
        dto.setCodcus(customerId);
        dto.setOrdsts(OrderStatus.PENDING);
        dto.setOrdpaydue(LocalDate.now().minusDays(1));
        dto.setItems(List.of(new ItemRequestDto(productId, 1, null)));

        AppException ex = assertThrows(AppException.class, () -> orderService.updateOrder(orderId ,dto));
        assertEquals("Cannot create a pending order with a payment due date in the past. Use OVERDUE, PAID or CANCELLED instead.", ex.getMessage());
    }

    //Insufficient stock test
    @Test
    void shouldThrowExceptionWhenInsufficientStockOnUpdate() {
        Long orderId = 999L;
        Order existingOrder = new Order();
        existingOrder.setCodord(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        Long customerId = 1L;
        Customer cus = new Customer();
        cus.setCodcus(customerId);
        existingOrder.setCodcus(cus);

        when(customerRepository.findBycodcusAndIsDeletedFalse(1L))
                .thenReturn(Optional.of(cus));

        Long  productId = 99L;

        Stock stock = new Stock();
        stock.setCodProd(productId);
        stock.setUnpricInCents(1000);
        stock.setQuantity(5);
        stock.setMinimumQtd(1);
        stock.setUntype(StockUnitType.UNIT);
        stock.setUnqtt(1);

        ItemRequestDto itemDto = new ItemRequestDto();
        itemDto.setCodprod(productId);
        itemDto.setQuantity(10);

        OrderRequestDto dto = new OrderRequestDto();
        dto.setCodcus(customerId);
        dto.setOrdsts(OrderStatus.PENDING);
        dto.setOrdpaydue(LocalDate.now());
        dto.setItems(List.of(itemDto));

        when(stockRepository.findById(productId))
                .thenReturn(Optional.of(stock));

        AppException ex = assertThrows(AppException.class, () -> orderService.updateOrder(orderId ,dto));
        String expectedMessage = String.format(
                "Insufficient stock for product ID: %d. Available: %d, Requested: %d",
                stock.getCodProd(), stock.getQuantity(), itemDto.getQuantity()
        );
        assertEquals(expectedMessage, ex.getMessage());
    }

    //Notify when stock falls below minimum test
    @Test
    void shouldThrowExceptionWhenStockFallsBelowMinimumOnUpdate() {
        Long orderId = 999L;
        Long customerId = 1L;
        Order existingOrder = new Order();
        existingOrder.setCodord(orderId);
        existingOrder.setOrdsts(OrderStatus.PAID);
        existingOrder.setOrdpaytype(OrderPaymentType.CREDIT);
        existingOrder.setOrdpaydue(LocalDate.now());
        existingOrder.setItems(new ArrayList<>());
        existingOrder.setOrdcostInCents(1000);
        existingOrder.setIsDeleted(false);
        Customer cus = new Customer();
        cus.setCodcus(customerId);
        existingOrder.setCodcus(cus);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        when(customerRepository.findBycodcusAndIsDeletedFalse(1L))
                .thenReturn(Optional.of(cus));

        Long productId = 99L;

        Stock stock = new Stock();
        stock.setCodProd(productId);
        stock.setUnpricInCents(100);
        stock.setUnqtt(1);
        stock.setUntype(StockUnitType.UNIT);
        stock.setQuantity(6);
        stock.setMinimumQtd(5);

        when(stockRepository.findById(productId))
                .thenReturn(Optional.of(stock));

        ItemRequestDto itemDto = new ItemRequestDto();
        itemDto.setCodprod(productId);
        itemDto.setQuantity(2);

        OrderRequestDto dto = new OrderRequestDto();
        dto.setCodcus(customerId);
        dto.setItems(List.of(itemDto));
        dto.setOrdsts(OrderStatus.PAID);
        dto.setOrdpaydue(LocalDate.now());
        dto.setOrdpaytype(OrderPaymentType.CREDIT);
        dto.setIsDeleted(false);

        when(stockRepository.save(any(Stock.class))).thenReturn(stock);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderMapper.toDto(any(Order.class))).thenReturn(new OrderResponseDto());

        orderService.updateOrder(orderId, dto);

        verify(orderNotificationService).notifyLowStock(stock);
    }

    // Order with multiple items succeed test
    @Test
    void shouldThrowExceptionWhenOrderWithMultipleItemsSucceedOnUpdate() {
        Long orderId = 999L;
        Long customerId = 1L;

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

        //Existing items
        Item existingItem1 = new Item();
        existingItem1.setCodprod(stock);
        existingItem1.setQuantity(10);
        existingItem1.setDiscountPercent(0.0F);

        Item existingItem2 = new Item();
        existingItem2.setCodprod(stock2);
        existingItem2.setQuantity(10);
        existingItem2.setDiscountPercent(0.0F);

        Item existingItem3 = new Item();
        existingItem3.setCodprod(stock3);
        existingItem3.setQuantity(15);
        existingItem3.setDiscountPercent(0.0F);

        //Existing order
        Order existingOrder = new Order();
        existingOrder.setCodord(orderId);
        existingOrder.setOrdsts(OrderStatus.PAID);
        existingOrder.setOrdpaytype(OrderPaymentType.CREDIT);
        existingOrder.setOrdpaydue(LocalDate.now());
        existingOrder.setItems(new ArrayList<>(List.of(existingItem1, existingItem2)));
        existingOrder.setOrdcostInCents(1000);
        existingOrder.setIsDeleted(false);
        Customer cus = new Customer();
        cus.setCodcus(customerId);
        existingOrder.setCodcus(cus);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        when(customerRepository.findBycodcusAndIsDeletedFalse(1L))
                .thenReturn(Optional.of(cus));

        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        when(stockRepository.findById(2L)).thenReturn(Optional.of(stock2));
        when(stockRepository.findById(3L)).thenReturn(Optional.of(stock3));

        when(stockRepository.save(any(Stock.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

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
        item3.setQuantity(15);
        item3.setDiscountPercent(0.0F);

        OrderRequestDto dto = new OrderRequestDto();
        dto.setCodcus(customerId);
        dto.setOrdpaytype(CREDIT);
        dto.setOrdsts(OrderStatus.PAID);
        dto.setOrdpaydue(LocalDate.now().plusDays(2));
        dto.setOrdnote("Pedido teste");
        dto.setOrdpaydue(LocalDate.now());
        dto.setItems(List.of(item1, item2, item3));
        dto.setIsDeleted(false);

        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setCodord(orderId);
        when(orderMapper.toDto(any(Order.class))).thenReturn(responseDto);

        OrderResponseDto result = orderService.updateOrder(orderId ,dto);

        assertNotNull(result);
        assertEquals(orderId, result.getCodord());

        verify(orderRepository).save(any(Order.class));
        verify(itemRepository, times(3)).save(any(Item.class));
        verify(orderMapper).toDto(any(Order.class));
        verify(orderNotificationService, never()).notifyLowStock(any());
    }
}
