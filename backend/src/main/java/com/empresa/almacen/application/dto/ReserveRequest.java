package com.empresa.almacen.application.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class ReserveRequest {
    private UUID productId;
    private UUID locationId;
    private Integer reservedQuantity;
}
