package com.empresa.almacen.adapters.outbound.persistence.entity;

import jakarta.persistence.*;
import lombok.*;


import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
@Table(name = "shipment_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentItemEntity {


@Id
@GeneratedValue(strategy = GenerationType.UUID)
private UUID id;


@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "shipment_id", nullable = false)
@JsonBackReference
private ShipmentEntity shipment;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "pick_order_id")
private PickOrderEntity pickOrder;


@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "product_id", nullable = false)
private ProductEntity product;


@Column(nullable = false)
private int qty;
}
