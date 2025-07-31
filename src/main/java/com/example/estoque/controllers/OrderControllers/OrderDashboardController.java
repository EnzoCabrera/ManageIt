package com.example.estoque.controllers.OrderControllers;

import com.example.estoque.dtos.orderDtos.DashboardDtos.DailyNumberOfOrders;
import com.example.estoque.dtos.orderDtos.DashboardDtos.DailyOrderAvgDto;
import com.example.estoque.dtos.orderDtos.DashboardDtos.DailyOrderSummaryDto;
import com.example.estoque.dtos.orderDtos.DashboardDtos.DailyOrdersDto;
import com.example.estoque.repositories.OrderRepositories.OrderDashboardRepository;
import com.example.estoque.services.OrderServices.OrderDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderDashboardController {

    private final OrderDashboardService orderDashboardService;

    public OrderDashboardController(OrderDashboardService orderDashboardService) {
        this.orderDashboardService = orderDashboardService;
    }

    //GET current day orders
    @GetMapping("/see/daily-summary")
    public ResponseEntity<List<DailyOrderSummaryDto>> getDailyOrderSummary() {
        return ResponseEntity.ok(orderDashboardService.getDailyOrderSummary());
    }

    //GET current day total orders
    @GetMapping("/see/daily-total")
    public ResponseEntity<List<DailyNumberOfOrders>> getDailyNumberOfOrders() {
        return ResponseEntity.ok(orderDashboardService.getDailyNumberOfOrders());
    }

    //GET current day orders average cost
    @GetMapping("/see/daily-avg")
    public ResponseEntity<List<DailyOrderAvgDto>> getDailyOrderAvg() {
        return ResponseEntity.ok(orderDashboardService.getDailyOrderAvg());
    }

    //GET current day orders
    @GetMapping("/see/daily-orders")
    public ResponseEntity<List<DailyOrdersDto>> getDailyOrders() {
        return ResponseEntity.ok(orderDashboardService.getDailyOrders());
    }
}
