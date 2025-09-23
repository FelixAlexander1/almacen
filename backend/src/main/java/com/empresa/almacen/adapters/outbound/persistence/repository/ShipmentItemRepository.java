package com.empresa.almacen.adapters.outbound.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.empresa.almacen.adapters.outbound.persistence.entity.ShipmentItemEntity;

import java.util.List;
import java.util.UUID;


public interface ShipmentItemRepository extends JpaRepository<ShipmentItemEntity, UUID> {
List<ShipmentItemEntity> findByShipmentId(UUID shipmentId);
}
