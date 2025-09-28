package com.empresa.almacen.application.service;


import java.util.List;
import java.util.UUID;

import com.empresa.almacen.application.dto.InventoryCountDTO;
import com.empresa.almacen.application.dto.InventoryItemDTO;
import com.empresa.almacen.application.dto.InventoryTransferDTO;

public interface InventoryItemService {

    List<InventoryItemDTO> search(UUID productId, UUID locationId);

    void transfer(InventoryTransferDTO dto);

    void count(InventoryCountDTO dto);

    InventoryItemDTO create(InventoryItemDTO dto);

    void adjust(InventoryCountDTO dto);

    void reserveStock(UUID productId, UUID locationId, int reservedQuantity);

}
