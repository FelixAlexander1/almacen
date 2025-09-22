package com.empresa.almacen.application.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingRequestDTO {
    private UUID shipmentId;
}
