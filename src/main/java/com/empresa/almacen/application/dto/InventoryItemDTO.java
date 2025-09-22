package com.empresa.almacen.application.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemDTO {
    private UUID id;
    private UUID productId; 
    private UUID locationId;
    private ProductDTO product;  
    private LocationDTO location;
    private int quantityTotal;
    private int quantityReserved;
    private String lotNumber;
    private LocalDate expiryDate;
}

