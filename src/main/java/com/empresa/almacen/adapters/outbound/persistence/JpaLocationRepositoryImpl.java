
package com.empresa.almacen.adapters.outbound.persistence;

import com.empresa.almacen.adapters.outbound.persistence.entity.LocationEntity;
import com.empresa.almacen.adapters.outbound.persistence.repository.LocationJpaRepository;
import com.empresa.almacen.application.mapper.LocationMapper;
import com.empresa.almacen.domain.model.Location;
import com.empresa.almacen.domain.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaLocationRepositoryImpl implements LocationRepository { // dominio
    private final LocationJpaRepository jpaRepository; // Spring Data
    private final LocationMapper mapper;

    @Override
    public Location save(Location location) {
        LocationEntity entity = mapper.toEntity(location);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Location> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Location> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Location> findByCode(String code) {
        return jpaRepository.findByCode(code).map(mapper::toDomain);
    }
}

