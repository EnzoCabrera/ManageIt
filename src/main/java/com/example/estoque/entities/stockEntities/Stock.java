package com.example.estoque.entities.stockEntities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name="TGVSTO")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "codProd")
@EntityListeners(AuditingEntityListener.class)
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codprod")
    private Long codProd;

    @Column(name="product_name")
    private String productName;

    private Integer quantity;

    @Column(name = "unpric_in_cents")
    private Integer unpricInCents;

    @Enumerated(EnumType.STRING)
    @Column(name = "untype", nullable = false)
    private StockUnitType untype;

    private Integer unqtt;

    @Column(name = "mininum_qtd")
    private Integer minimumQtd;

    @CreationTimestamp
    @CreatedDate
    @Column(name = "created_at" ,updatable = false)
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

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
