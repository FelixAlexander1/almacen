package com.empresa.almacen.adapters.outbound.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.empresa.almacen.adapters.outbound.persistence.entity.InboundItemEntity;

import java.util.List;
import java.util.UUID;


public interface InboundItemRepository extends JpaRepository<InboundItemEntity, UUID> {
List<InboundItemEntity> findByInboundId(UUID inboundId);
}
