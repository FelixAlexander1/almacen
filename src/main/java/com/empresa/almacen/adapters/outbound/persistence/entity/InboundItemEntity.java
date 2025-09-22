package com.empresa.almacen.adapters.outbound.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "inbound_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InboundItemEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inbound_id", nullable = false)
    private InboundEntity inbound;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "expected_qty", nullable = false)
    private int expectedQty;

    @Column(name = "received_qty", nullable = false)
    private int receivedQty;

    @Column(name = "lot")
    private String lot;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationEntity location;

}
