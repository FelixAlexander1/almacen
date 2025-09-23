package com.empresa.almacen.application.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class InventoryCountDTO {

    @NotNull
    private UUID productId;

    @NotNull
    private UUID locationId;

    @Min(0)
    private int countedQuantity;

    private String lotNumber; // Opcional
}

