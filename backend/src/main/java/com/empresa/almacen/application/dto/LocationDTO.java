package com.empresa.almacen.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {
    private UUID id;
    private String code;
    private String type;   // PICK, BUFFER, BULK, DOCK
    private int capacity;
}
