package com.empresa.almacen.adapters.outbound.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Table(name = "shipments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentEntity {


@Id
@GeneratedValue(strategy = GenerationType.UUID)
private UUID id;


private String carrier;


@Column(name = "tracking_number", unique = true)
private String trackingNumber;


@Enumerated(EnumType.STRING)
private ShipmentStatus status;

@OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL)
private List<PickOrderEntity> pickOrders = new ArrayList<>();

@OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL)
@JsonManagedReference
private List<ShipmentItemEntity> shipmentItems = new ArrayList<>();

public enum ShipmentStatus {
CREATED, IN_TRANSIT, DELIVERED, CANCELLED
}
}
