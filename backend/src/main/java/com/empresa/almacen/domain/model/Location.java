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
public class Location {
private UUID id;
private String code; // "A-01-01"
private String type; // PICK, BUFFER, BULK, DOCK
private int capacity; 
}
