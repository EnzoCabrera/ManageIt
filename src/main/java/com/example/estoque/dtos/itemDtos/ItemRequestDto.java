package com.example.estoque.dtos.itemDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemRequestDto {

    @NotNull
    private Long codprod;

    @NotNull
    private Integer quantity;

    private Float discountPercent;
}
