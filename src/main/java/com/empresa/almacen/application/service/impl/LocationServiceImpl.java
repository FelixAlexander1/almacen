package com.empresa.almacen.application.service.impl;


import com.empresa.almacen.application.dto.LocationDTO;
import com.empresa.almacen.application.mapper.LocationMapper;
import com.empresa.almacen.application.service.LocationService;
import com.empresa.almacen.domain.model.Location;
import com.empresa.almacen.domain.repository.LocationRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Override
    public LocationDTO create(LocationDTO dto) {
        Location location = locationMapper.toDomain(dto);
        Location saved = locationRepository.save(location);
        return locationMapper.toDTO(saved);
    }

    @Override
    public List<LocationDTO> getAll() {
        return locationRepository.findAll()
                .stream()
                .map(locationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public LocationDTO getById(UUID id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ubicación no encontrada"));
        return locationMapper.toDTO(location);
    }
}

