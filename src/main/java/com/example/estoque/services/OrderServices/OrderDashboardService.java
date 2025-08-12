package com.example.estoque.services.OrderServices;

import com.example.estoque.dtos.orderDtos.DashboardDtos.DailyNumberOfOrders;
import com.example.estoque.dtos.orderDtos.DashboardDtos.DailyOrderAvgDto;
import com.example.estoque.dtos.orderDtos.DashboardDtos.DailyOrderSummaryDto;
import com.example.estoque.dtos.orderDtos.DashboardDtos.DailyOrdersDto;
import com.example.estoque.exceptions.AppException;
import com.example.estoque.repositories.OrderRepositories.OrderDashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDashboardService {

    private final OrderDashboardRepository orderDashboardRepository;

    //GET current day orders total cost logic
    public List<DailyOrderSummaryDto> getDailyOrderSummary() {
        if (orderDashboardRepository.getDailyOrderSummary().isEmpty()) {
            throw new AppException("There are no orders today", HttpStatus.NOT_FOUND);
        }
        return orderDashboardRepository.getDailyOrderSummary();
    }

    //GET current day total orders logic
    public List<DailyNumberOfOrders> getDailyNumberOfOrders() {
        return orderDashboardRepository.getDailyNumberOfOrders();
    }

    //GET current day orders average cost logic
    public List<DailyOrderAvgDto> getDailyOrderAvg() {
        if (orderDashboardRepository.getDailyOrderAverage().isEmpty()) {
            throw new AppException("There are no orders today", HttpStatus.NOT_FOUND);
        }
        return orderDashboardRepository.getDailyOrderAverage();
    }

    //GET current day orders logic
    public List<DailyOrdersDto> getDailyOrders() {
        if (orderDashboardRepository.getDailyOrders().isEmpty()) {
            throw new AppException("There are no orders today", HttpStatus.NOT_FOUND);
        }
        return orderDashboardRepository.getDailyOrders();
    }
}
