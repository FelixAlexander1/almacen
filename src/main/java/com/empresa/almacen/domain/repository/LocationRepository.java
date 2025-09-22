package com.empresa.almacen.domain.repository;

import com.empresa.almacen.domain.model.Location;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocationRepository {
    Location save(Location location);
    Optional<Location> findById(UUID id);
    List<Location> findAll();
    Optional<Location> findByCode(String code);
}
