package com.empresa.almacen.application.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private UUID id;
    private String sku;
    private String name;
    private String description;
    private String uom; // Unidad de medida
    private double length;
    private double width;
    private double height;
    private double weight;
    private boolean hazardous;
}
