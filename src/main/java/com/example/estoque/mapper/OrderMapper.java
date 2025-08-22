package com.example.estoque.mapper;

import com.example.estoque.dtos.orderDtos.OrderResponseDto;
import com.example.estoque.entities.OrderEntities.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final ItemMapper itemMapper;

    public OrderResponseDto toDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setCodord(order.getCodord());
        dto.setCodcus(order.getCodcus().getCodcus());
        dto.setOrdcostInCents(order.getOrdcostInCents());
        dto.setOrdsts(order.getOrdsts().name());
        dto.setOrdpaytype(order.getOrdpaytype().name());
        dto.setOrdpaydue(order.getOrdpaydue());
        dto.setOrdnote(order.getOrdnote());
        dto.setIsDeleted(order.getIsDeleted());

        dto.setItems(
                order.getItems()
                        .stream()
                        .map(itemMapper::toDto)
                        .collect(Collectors.toList())
        );

        return dto;
    }
}
