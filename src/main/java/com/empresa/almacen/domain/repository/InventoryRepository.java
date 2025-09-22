package com.empresa.almacen.domain.repository;

import com.empresa.almacen.domain.model.InventoryItem;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository {
    List<InventoryItem> findByFilters(UUID productId, UUID locationId);
    Optional<InventoryItem> findByProductAndLocation(UUID productId, UUID locationId);
    InventoryItem save(InventoryItem item);
    void delete(InventoryItem item);
    Optional<InventoryItem> findById(UUID id);
    
}
