package com.example.estoque.controllers.OrderControllers;

import com.example.estoque.config.Pageable.AllowedSort;
import com.example.estoque.dtos.authDtos.PageResponseDto;
import com.example.estoque.dtos.orderDtos.OrderRequestDto;
import com.example.estoque.dtos.orderDtos.OrderResponseDto;
import com.example.estoque.services.OrderServices.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    //GET orders
    @GetMapping("/see")
    public ResponseEntity<PageResponseDto<OrderResponseDto>> getOrders(
            @RequestParam(required = false) Long codord,
            @RequestParam(required = false) Long codcus,
            @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String ordsts,
            @RequestParam(required = false) String ordpaytype,
            @AllowedSort(value = {"codord", "codcus", "startDate", "endDate", "ordsts", "ordpaytype", "createdAt", "updatedAt"},
                         defaultProp = "createdAt", defaultDir = "DESC")
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(orderService.getOrderSlim(codord, codcus, startDate, endDate, ordsts, ordpaytype, pageable));
    }

    //POST order
    @PostMapping("/register")
    public ResponseEntity<OrderResponseDto> registerOrder(@RequestBody OrderRequestDto dto) {
        OrderResponseDto created = orderService.registerOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    //PUT order
    @PutMapping("/update/{codord}")
    public ResponseEntity<OrderResponseDto> registerOrder(@RequestBody OrderRequestDto dto, @PathVariable Long codord) {
        OrderResponseDto updated = orderService.updateOrder(codord, dto);
        return ResponseEntity.ok(updated);
    }

    //DELETE order
    @DeleteMapping("/delete/{codord}")
    public ResponseEntity<OrderResponseDto> deleteOrder(@PathVariable Long codord) {
        OrderResponseDto deleted =  orderService.deleteOrder(codord);
        return ResponseEntity.ok(deleted);
    }
}
