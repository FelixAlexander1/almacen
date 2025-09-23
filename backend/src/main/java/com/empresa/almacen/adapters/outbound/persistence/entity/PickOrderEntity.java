package com.empresa.almacen.adapters.outbound.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "pick_orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PickOrderEntity {


@Id
@GeneratedValue(strategy = GenerationType.UUID)
private UUID id;


@Column(nullable = false, unique = true)
private String reference;


@Enumerated(EnumType.STRING)
private PickOrderStatus status;


private int priority;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "shipment_id")
private ShipmentEntity shipment;



@OneToMany(mappedBy = "pickOrder", cascade = CascadeType.ALL, orphanRemoval = true)
private List<PickOrderLineEntity> lines = new ArrayList<>();


public enum PickOrderStatus {
    CREATED,
    PICKED,
    COMPLETED
}
}
