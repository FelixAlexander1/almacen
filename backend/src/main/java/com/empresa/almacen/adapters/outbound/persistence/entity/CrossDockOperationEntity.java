package com.empresa.almacen.adapters.outbound.persistence.entity;

import jakarta.persistence.*;
import lombok.*;


import java.util.UUID;


@Entity
@Table(name = "crossdock_operations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrossDockOperationEntity {


@Id
@GeneratedValue(strategy = GenerationType.UUID)
private UUID id;


@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "inbound_id", nullable = false)
private InboundEntity inbound;


@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "out_shipment_id", nullable = false)
private ShipmentEntity outShipment;


@Enumerated(EnumType.STRING)
private CrossDockStatus status;


public enum CrossDockStatus {
CREATED, PROCESSING, COMPLETED
}

@Version
@Column(nullable = false)
private Long version;

}
