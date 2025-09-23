package com.empresa.almacen.adapters.outbound.persistence.entity;

import jakarta.persistence.*;
import lombok.*;


import java.util.UUID;


@Entity
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {


@Id
@GeneratedValue(strategy = GenerationType.UUID)
private UUID id;


@Column(nullable = false, unique = true)
private String sku;


@Column(nullable = false)
private String name;


private String description;


@Column(nullable = false)
private String uom;


private double length;
private double width;
private double height;
private double weight;


@Column(nullable = false)
private boolean hazardous;
}
