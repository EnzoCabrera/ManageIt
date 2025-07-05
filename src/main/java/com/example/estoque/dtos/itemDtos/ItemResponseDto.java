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
public class ItemResponseDto {

    @NotNull
    private Long codite;

    @NotNull
    private Long codord;

    @NotNull
    private Long codprod;

    @NotNull
    private Integer quantity;

    @NotNull
    private Integer priceInCents;

    private Float discountPercent;

}
