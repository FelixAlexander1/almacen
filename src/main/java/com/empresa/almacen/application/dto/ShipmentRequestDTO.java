package com.empresa.almacen.application.dto;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ShipmentRequestDTO {

    private String carrier;
    private String trackingNumber;
    private String status;
    private List<UUID> pickOrderIds;

    private List<ShipmentItemDTO> shipmentItems;

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    public static class ShipmentItemDTO {
        private UUID productId;
        private Integer qty;
    }
}

