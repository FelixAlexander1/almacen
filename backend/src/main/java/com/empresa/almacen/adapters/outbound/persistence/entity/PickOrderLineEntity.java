package com.empresa.almacen.adapters.outbound.persistence.entity;

import jakarta.persistence.*;
import lombok.*;


import java.util.UUID;


@Entity
@Table(name = "pick_order_lines")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PickOrderLineEntity {


@Id
@GeneratedValue(strategy = GenerationType.UUID)
private UUID id;


@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "pick_order_id", nullable = false)
private PickOrderEntity pickOrder;


@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "product_id", nullable = false)
private ProductEntity product;


@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "location_id", nullable = false)
private LocationEntity location;


@Column(nullable = false)
private int qty;


@Column(name = "picked_qty", nullable = false)
private int pickedQty;
}
