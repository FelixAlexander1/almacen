package com.empresa.almacen.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
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
