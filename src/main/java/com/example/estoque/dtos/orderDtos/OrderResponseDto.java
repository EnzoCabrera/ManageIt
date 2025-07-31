package com.example.estoque.dtos.orderDtos;

import com.example.estoque.dtos.itemDtos.ItemRequestDto;
import com.example.estoque.dtos.itemDtos.ItemResponseDto;
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
public class OrderResponseDto {

    @NotNull
    private Long codord;

    @NotNull
    private Integer ordcostInCents;

    @NotNull
    private Long codcus;

    @NotNull
    private String ordsts;

    @NotNull
    private String ordpaytype;

    @NotNull
    private LocalDate ordpaydue;

    private String ordnote;

    private List<ItemResponseDto> items;


}
