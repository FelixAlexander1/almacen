package com.empresa.almacen.application.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrossDockOperationDTO {
    private UUID id;
    private UUID inboundId;
    private UUID outShipmentId;
    private String status; // CREATED, PROCESSING, COMPLETED
}
