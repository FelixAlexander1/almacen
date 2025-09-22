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
public class PickOrder {
private UUID id;
private String reference; // Pedido cliente/interno
private List<PickOrderLine> lines;
private PickOrderStatus status;
private int priority;


public enum PickOrderStatus {
    CREATED,
    PICKED,
    COMPLETED
}


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public static class PickOrderLine {
private UUID productId;
private int qty;
private int pickedQty;
private UUID locationId;
}
}
