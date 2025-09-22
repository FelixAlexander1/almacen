package com.empresa.almacen.application.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PickOrderDTO {
    private UUID id;
    private String reference;
    private List<PickOrderLineDTO> lines = new ArrayList<>();
    private String status; // CREATED, ASSIGNED, PICKED, PACKED, SHIPPED
    private int priority;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PickOrderLineDTO {
        private UUID productId;
        private int qty;
        private int pickedQty;
        private UUID locationId;

    }
}