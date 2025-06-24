package com.example.estoque.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Table(name="stock")
@Entity(name="stock")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "cod_produto")
public class Stock {
    @Id
    private Long cod_produto;

    @Column
    private String product_name;

    @Column
    private Integer price_in_cents;

    @Column
    private Integer quantity;

    @CreationTimestamp
    @Column
    private LocalDateTime created_at;
}
