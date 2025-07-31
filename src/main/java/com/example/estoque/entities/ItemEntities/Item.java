package com.example.estoque.entities.ItemEntities;

import com.example.estoque.entities.OrderEntities.Order;
import com.example.estoque.entities.stockEntities.Stock;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "TGVITE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "codite")
@EntityListeners(value = AuditingEntityListener.class)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long codite;

    @ManyToOne
    @JoinColumn(nullable = false, name = "codord")
    private Order codord;

    @ManyToOne
    @JoinColumn (nullable = false, name = "codprod")
    private Stock codprod;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "price_in_cents", nullable = false)
    private Integer priceInCents;

    @Column(name = "discount_percent")
    private Float discountPercent;

    @Column(name = "total_in_cents", nullable = false)
    private Integer totalInCents;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;
}
