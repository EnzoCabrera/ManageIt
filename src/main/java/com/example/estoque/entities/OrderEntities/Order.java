package com.example.estoque.entities.OrderEntities;

import com.example.estoque.entities.ItemEntities.Item;
import com.example.estoque.entities.customerEntities.Customer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TGVORD")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "codord")
@EntityListeners(value = AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codord;

    @Column(name = "ordcost_in_cents", nullable = false)
    private Integer ordcostInCents;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Customer codcus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus ordsts;

    @Enumerated(EnumType.STRING)
    @Column(name = "ordpaytype")
    private OrderPaymentType ordpaytype;

    private LocalDate ordpaydue;

    private String ordnote;

    @OneToMany(mappedBy = "codord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items;

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
