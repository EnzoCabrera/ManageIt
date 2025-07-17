package com.example.estoque.services.OrderServices;

import com.example.estoque.dtos.itemDtos.ItemRequestDto;
import com.example.estoque.dtos.orderDtos.OrderRequestDto;
import com.example.estoque.dtos.orderDtos.OrderResponseDto;
import com.example.estoque.entities.ItemEntities.Item;
import com.example.estoque.entities.OrderEntities.Order;
import com.example.estoque.entities.OrderEntities.OrderPaymentType;
import com.example.estoque.entities.OrderEntities.OrderSpecification;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository OrderRespository;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private OrderNotificationService orderNotificationService;

    //GET orders logic
    public List<OrderResponseDto> getFilterOrders(
          Long codord,
          Long codcus,
          LocalDate startDate,
          LocalDate endDate,
          String ordsts,
          String ordpaytype
    ) {

        Specification<Order> spec = Specification.where(OrderSpecification.isNotDeleted())
                .and(OrderSpecification.hasCodord(codord))
                .and(OrderSpecification.hasCustomerCode(codcus))
                .and(OrderSpecification.isBetweenCreatedAt(startDate, endDate))
                .and(OrderSpecification.hasOrderSts(ordsts))
                .and(OrderSpecification.hasOrderPayType(ordpaytype));

        return orderRepository.findAll(spec)
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    //POST order logic
    @Transactional
    public OrderResponseDto registerOrder(OrderRequestDto dto) {

        Customer customer = customerRepository.findBycodcusAndIsDeletedFalse(dto.getCodcus())
                .orElseThrow(() -> new AppException("Customer not found or disabled", HttpStatus.BAD_REQUEST));

        Order order = new Order();
        order.setOrdsts(dto.getOrdsts());
        order.setCodcus(customer);
        order.setOrdpaytype(dto.getOrdpaytype());
        order.setOrdpaydue(dto.getOrdpaydue());
        order.setOrdnote(dto.getOrdnote());

        List<Item> items = new ArrayList<>();
        int totalCost = 0;

        for (ItemRequestDto itemDto : dto.getItems()) {
            Stock stock = stockRepository.findById(itemDto.getCodprod())
                    .orElseThrow(() -> new AppException("Product not found", HttpStatus.BAD_REQUEST));

            if (stock.getUnpricInCents() <= 0.0) {
                throw new AppException("Product should have a unit price", HttpStatus.BAD_REQUEST);
            }

            if (dto.getOrdsts() == OrderStatus.PENDING &&
                dto.getOrdpaydue().isBefore(LocalDate.now())) {
                throw new AppException("Cannot create a pending order with a payment due date in the past. Use OVERDUE, PAID or CANCELLED instead.", HttpStatus.BAD_REQUEST);
            }

            int availableQtd = stock.getQuantity();
            int requestedQtd = itemDto.getQuantity();

            if (requestedQtd > availableQtd) {
                throw new AppException(String.format(
                        "Insufficient stock for product ID: %d. Available: %d, Requested: %d",
                        stock.getCodProd(), availableQtd, requestedQtd
                ), HttpStatus.BAD_REQUEST);
            }

            stock.setQuantity(availableQtd - requestedQtd);
            stockRepository.save(stock);

            // Notify if stock is below minimum quantity
            if (stock.getQuantity() < stock.getMinimumQtd()) {
                orderNotificationService.notifyLowStock(stock);
            }

            int unitPrice = stock.getUnpricInCents();
            int unitQuantity = stock.getUnqtt();
            String unitType = stock.getUntype().name();

            int finalUnicPrice;
            if (unitType.equals("UNIT")) {
                finalUnicPrice = unitPrice;
            }
            finalUnicPrice = unitPrice * unitQuantity;

            double discountPercent = itemDto.getDiscountPercent() != null ? itemDto.getDiscountPercent() : 0.0;

            int itemCost = (int) Math.round(finalUnicPrice * requestedQtd * (1 - (discountPercent / 100.0)));

            Item item = new Item();
            item.setCodord(order);
            item.setCodprod(stock);
            item.setQuantity(requestedQtd);
            item.setPriceInCents(unitPrice);
            item.setDiscountPercent((float) discountPercent);
            item.setTotalInCents(itemCost);

            items.add(item);
            totalCost += itemCost;
        }

        order.setOrdcostInCents(totalCost);
        order.setItems(items);
        Order savedOrder = orderRepository.save(order);

        // Log the creation of the order
        auditLogService.log(
                "Order",
                savedOrder.getCodord(),
                "CREATE",
                null,
                null,
                null,
                savedOrder.getCreatedBy()
        );

        for (Item item : items) {
            itemRepository.save(item);
        }

        return orderMapper.toDto(savedOrder);
    }

    //PUT order logic
    @Transactional
    public OrderResponseDto updateOrder(Long codord, OrderRequestDto dto) {
        Order order = OrderRespository.findById(codord)
                .orElseThrow(() -> new AppException("Order not found or deleted.", HttpStatus.NOT_FOUND));

        // Capture old values for logging
        OrderStatus oldStatus = order.getOrdsts();
        OrderPaymentType oldPayType = order.getOrdpaytype();
        LocalDate oldPayDue = order.getOrdpaydue();
        Integer oldTotalCost = order.getOrdcostInCents();
        Long oldCustomer = order.getCodcus().getCodcus();

        order.setOrdsts(dto.getOrdsts());
        order.setOrdpaytype(dto.getOrdpaytype());
        order.setOrdpaydue(dto.getOrdpaydue());
        order.setOrdnote(dto.getOrdnote());

        Customer customer = customerRepository.findBycodcusAndIsDeletedFalse(dto.getCodcus())
                .orElseThrow(() -> new AppException("Customer not found or disabled", HttpStatus.BAD_REQUEST));

        order.setCodcus(customer);

        if (order.getItems() != null) {
            for (Item oldItem : order.getItems()) {
                Stock stock = oldItem.getCodprod();
                stock.setQuantity(stock.getQuantity() + oldItem.getQuantity());
                stockRepository.save(stock);
            }
        }

        order.getItems().clear();

        List<Item> updatedItems = new ArrayList<>();
        int totalCost = 0;

        for (ItemRequestDto itemDto : dto.getItems()) {
            Stock stock = stockRepository.findById(itemDto.getCodprod())
                    .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));

            if (stock.getUnpricInCents() <= 0.0) {
                throw new AppException("Product should have a unit price", HttpStatus.BAD_REQUEST);
            }

            if (dto.getOrdsts() == OrderStatus.PENDING &&
                    dto.getOrdpaydue().isBefore(LocalDate.now())) {
                throw new AppException("Cannot create a pending order with a payment due date in the past. Use OVERDUE, PAID or CANCELLED instead.", HttpStatus.BAD_REQUEST);
            }

            int availableQtd = stock.getQuantity();
            int requestedQtd = itemDto.getQuantity();

            if (requestedQtd > availableQtd) {
                throw new AppException(String.format(
                        "Insufficient stock for product ID: %d. Available: %d, Requested: %d",
                        stock.getCodProd(), availableQtd, requestedQtd
                ), HttpStatus.BAD_REQUEST);
            }

            stock.setQuantity(availableQtd - requestedQtd);
            stockRepository.save(stock);

            // Notify if stock is below minimum quantity
            if (stock.getQuantity() < stock.getMinimumQtd()) {
                orderNotificationService.notifyLowStock(stock);
            }

            int unitPrice = stock.getUnpricInCents();
            int unitQuantity = stock.getUnqtt();
            String unitType = stock.getUntype().name();

            int finalUnicPrice;
            if (unitType.equals("UNIT")) {
                finalUnicPrice = unitPrice;
            }
            finalUnicPrice = unitPrice * unitQuantity;

            double discountPercent = itemDto.getDiscountPercent() != null ? itemDto.getDiscountPercent() : 0.0;

            int itemCost = (int) Math.round(finalUnicPrice * requestedQtd * (1 - (discountPercent / 100.0)));


            Item item = new Item();
            item.setCodord(order);
            item.setCodprod(stock);
            item.setQuantity(requestedQtd);
            item.setPriceInCents(unitPrice);
            item.setDiscountPercent((float) discountPercent);
            item.setTotalInCents(itemCost);

            order.getItems().add(item);
            totalCost += itemCost;
        }

        order.setOrdcostInCents(totalCost);
        Order savedOrder = orderRepository.save(order);

        String actor = savedOrder.getUpdatedBy();

        // Log the update of the order

        //STATUS
        if(!oldStatus.equals(savedOrder.getOrdsts())) {
            auditLogService.log("Order", savedOrder.getCodord(), "UPDATE",
                    "ordsts", oldStatus != null ? oldStatus.name() : null,
                    savedOrder.getOrdsts().name(), actor);
        }

        //PAYMENT TYPE
        if (!oldPayType.equals(savedOrder.getOrdpaytype())) {
            auditLogService.log("Order", savedOrder.getCodord(), "UPDATE",
                    "ordpaytype",
                    oldPayType != null ? oldPayType.name() : null,
                    savedOrder.getOrdpaytype().name(),
                    actor);
        }

        // Payment due
        if (!oldPayDue.equals(savedOrder.getOrdpaydue())) {
            auditLogService.log("Order", savedOrder.getCodord(), "UPDATE",
                    "ordpaydue",
                    oldPayDue != null ? oldPayDue.toString() : null,
                    savedOrder.getOrdpaydue().toString(),
                    actor);
        }

        // Total cost
        if (!oldTotalCost.equals(savedOrder.getOrdcostInCents())) {
            auditLogService.log("Order", savedOrder.getCodord(), "UPDATE",
                    "ordcost_in_cents",
                    oldTotalCost.toString(),
                    savedOrder.getOrdcostInCents().toString(),
                    actor);
        }

        // Customer
        if (!oldCustomer.equals(savedOrder.getCodcus().getCodcus())) {
            auditLogService.log("Order", savedOrder.getCodord(), "UPDATE",
                    "codcus",
                    oldCustomer.toString(),
                    savedOrder.getCodcus().getCodcus().toString(),
                    actor);
        }

        for (Item item : updatedItems) {
            itemRepository.save(item);
        }

        return orderMapper.toDto(savedOrder);
    }

    //DELETE order logic
    @Transactional
    public OrderResponseDto deleteOrder(Long codord) {
        Order order = OrderRespository.findBycodordAndIsDeletedFalse(codord)
                .orElseThrow(() -> new AppException("Order not found or deleted.", HttpStatus.NOT_FOUND));

        order.setIsDeleted(true);

        if (order.getItems() != null) {
            order.getItems().forEach(item -> item.setIsDeleted(true));
        }

        Order updatedOrder = OrderRespository.save(order);

        // Log the exclusion of the order
        auditLogService.log(
                "Order",
                updatedOrder.getCodord(),
                "DELETE",
                null,
                null,
                null,
                updatedOrder.getUpdatedBy()
        );


        return orderMapper.toDto(updatedOrder);
    }
}

