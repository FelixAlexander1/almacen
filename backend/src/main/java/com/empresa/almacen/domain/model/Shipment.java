package com.empresa.almacen.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {
private UUID id;
private String carrier;
private String trackingNumber;
private List<ShipmentItem> shipmentItems;
private ShipmentStatus status;


public enum ShipmentStatus {
CREATED, IN_TRANSIT, DELIVERED, CANCELLED
}


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public static class ShipmentItem {
private UUID productId;
private int qty;
}
}
