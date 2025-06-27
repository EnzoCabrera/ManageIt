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
@EqualsAndHashCode(of = "codProd")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codprod")
    private Long codProd;

    @Column(name="product_name")
    private String productName;

    @Column(name="price_in_cents")
    private Integer PriceInCents;

    private Integer quantity;

    @CreationTimestamp
    @Column
    private LocalDateTime created_at;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}
