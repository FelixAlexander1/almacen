package com.empresa.almacen.application.service;

import com.empresa.almacen.application.dto.LocationDTO;

import java.util.List;
import java.util.UUID;

public interface LocationService {
    LocationDTO create(LocationDTO dto);
    List<LocationDTO> getAll();
    LocationDTO getById(UUID id);
}

