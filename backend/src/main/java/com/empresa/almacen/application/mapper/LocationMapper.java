package com.empresa.almacen.application.mapper;

import com.empresa.almacen.adapters.outbound.persistence.entity.LocationEntity;
import com.empresa.almacen.application.dto.LocationDTO;
import com.empresa.almacen.domain.model.Location;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    // Entity ↔ Domain
    Location toDomain(LocationEntity entity);
    LocationEntity toEntity(Location domain);

    // Domain ↔ DTO
    LocationDTO toDTO(Location domain);
    Location toDomain(LocationDTO dto);
}

