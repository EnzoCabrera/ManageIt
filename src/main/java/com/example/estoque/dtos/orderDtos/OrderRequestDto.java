package com.example.estoque.dtos.orderDtos;

import com.example.estoque.dtos.itemDtos.ItemRequestDto;
import com.example.estoque.entities.OrderEntities.OrderPaymentType;
import com.example.estoque.entities.OrderEntities.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderRequestDto {

    @NotNull
    private Long codcus;

    @NotNull
    private OrderStatus ordsts;

    @NotNull
    private OrderPaymentType ordpaytype;

    @NotNull
    private LocalDate ordpaydue;

    private String ordnote;

    private List<ItemRequestDto> items;
}
