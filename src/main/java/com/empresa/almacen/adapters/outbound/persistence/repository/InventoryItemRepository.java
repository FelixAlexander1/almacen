package com.empresa.almacen.adapters.outbound.persistence.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.empresa.almacen.adapters.outbound.persistence.entity.InventoryItemEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface InventoryItemRepository extends JpaRepository<InventoryItemEntity, UUID> {
List<InventoryItemEntity> findByProductId(UUID productId);
List<InventoryItemEntity> findByLocationId(UUID locationId);
Optional<InventoryItemEntity> findByProductIdAndLocationId(UUID productId, UUID locationId);
}
