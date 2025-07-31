package com.example.estoque.dtos.stockDtos;

import com.example.estoque.entities.stockEntities.StockUnitType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StockRequestDto {

    @NotNull
    private String productName;

    @NotNull
    private Integer quantity;

    @NotNull
    private Integer unpricInCents;

    @NotNull
    private StockUnitType untype;

    @NotNull
    private Integer unqtt;

    @NotNull
    private Integer minimumQtd;
}
