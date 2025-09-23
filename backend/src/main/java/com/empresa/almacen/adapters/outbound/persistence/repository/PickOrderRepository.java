package com.empresa.almacen.adapters.outbound.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.empresa.almacen.adapters.outbound.persistence.entity.PickOrderEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface PickOrderRepository extends JpaRepository<PickOrderEntity, UUID> {
List<PickOrderEntity> findByStatus(PickOrderEntity.PickOrderStatus status);
Optional<PickOrderEntity> findByReference(String reference);
}
