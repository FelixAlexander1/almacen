package com.empresa.almacen.application.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentDTO {
    private UUID id;
    private String carrier;
    private String trackingNumber;
    private List<ShipmentItemDTO> shipmentItems;
    private String status;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShipmentItemDTO {
        private UUID productId;
        private int qty;
    }
}
