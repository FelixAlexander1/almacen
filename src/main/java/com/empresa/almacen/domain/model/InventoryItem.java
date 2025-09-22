package com.empresa.almacen.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItem {
private UUID id;
private UUID productId;
private UUID locationId;
private int quantityTotal;
private int quantityReserved;
private String lotNumber;
private LocalDate expiryDate;



public int getAvailableQuantity() {
return quantityTotal - quantityReserved;
}
}
