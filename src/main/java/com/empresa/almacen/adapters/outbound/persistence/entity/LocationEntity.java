package com.empresa.almacen.adapters.outbound.persistence.entity;

import jakarta.persistence.*;
import lombok.*;


import java.util.UUID;


@Entity
@Table(name = "locations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationEntity {


@Id
@GeneratedValue(strategy = GenerationType.UUID)
private UUID id;


@Column(nullable = false, unique = true)
private String code; // Ejemplo: "A-01-01"


@Column(nullable = false)
private String type; // PICK, BUFFER, BULK, DOCK


private int capacity; // volumen/unidades
}
