package com.empresa.almacen.adapters.outbound.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.empresa.almacen.adapters.outbound.persistence.entity.LocationEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.PickOrderEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.PickOrderLineEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.ProductEntity;

import java.util.List;
import java.util.UUID;


public interface PickOrderLineRepository extends JpaRepository<PickOrderLineEntity, UUID> {
List<PickOrderLineEntity> findByPickOrderId(UUID pickOrderId);
List<PickOrderLineEntity> findByProductAndLocationAndPickOrderStatus(
            ProductEntity product,
            LocationEntity location,
            PickOrderEntity.PickOrderStatus status
    );

}
