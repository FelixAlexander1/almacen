package com.empresa.almacen.adapters.outbound.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.empresa.almacen.adapters.outbound.persistence.entity.ShipmentEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ShipmentRepository extends JpaRepository<ShipmentEntity, UUID> {
Optional<ShipmentEntity> findByTrackingNumber(String trackingNumber);
List<ShipmentEntity> findByStatus(ShipmentEntity.ShipmentStatus status);

}
