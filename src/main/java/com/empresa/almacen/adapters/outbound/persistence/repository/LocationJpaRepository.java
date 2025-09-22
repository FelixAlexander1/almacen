package com.empresa.almacen.adapters.outbound.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.empresa.almacen.adapters.outbound.persistence.entity.LocationEntity;
import java.util.Optional;
import java.util.UUID;

public interface LocationJpaRepository extends JpaRepository<LocationEntity, UUID> {
    Optional<LocationEntity> findByCode(String code);
}

