package com.example.estoque.dtos;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StockDto {

    @NotNull
    private Long codProduto;

    @NotNull
    private String product_name;

    @NotNull
    private Integer price_in_cents;

    @NotNull
    private Integer quantity;
}
