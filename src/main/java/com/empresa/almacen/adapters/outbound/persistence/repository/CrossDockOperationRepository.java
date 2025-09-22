package com.empresa.almacen.adapters.outbound.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.empresa.almacen.adapters.outbound.persistence.entity.CrossDockOperationEntity;

import java.util.List;
import java.util.UUID;


public interface CrossDockOperationRepository extends JpaRepository<CrossDockOperationEntity, UUID> {
List<CrossDockOperationEntity> findByStatus(CrossDockOperationEntity.CrossDockStatus status);
}
