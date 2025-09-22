package com.empresa.almacen.adapters.outbound.persistence.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.empresa.almacen.adapters.outbound.persistence.entity.ProductEntity;

import java.util.Optional;
import java.util.UUID;


public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
Optional<ProductEntity> findBySku(String sku);
}