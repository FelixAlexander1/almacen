package com.empresa.almacen.adapters.outbound.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.empresa.almacen.adapters.outbound.persistence.entity.InboundEntity;

import java.util.Optional;
import java.util.UUID;


public interface InboundRepository extends JpaRepository<InboundEntity, UUID> {
Optional<InboundEntity> findByAsnNumber(String asnNumber);
}