package com.empresa.almacen.adapters.inbound.rest;

import com.empresa.almacen.adapters.outbound.persistence.repository.LocationJpaRepository;
import com.empresa.almacen.application.dto.LocationDTO;
import com.empresa.almacen.application.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;
    private final LocationJpaRepository locationRepository;

    @GetMapping
    public List<LocationDTO> listLocations() {
        return locationRepository.findAll()
                .stream()
                .map(location -> LocationDTO.builder()
                        .id(location.getId())
                        .code(location.getCode())
                        .type(location.getType())
                        .capacity(location.getCapacity())
                        .build())
                .toList();
    }

    // Crear nueva ubicación
    @PostMapping
    public ResponseEntity<LocationDTO> create(@RequestBody LocationDTO dto) {
        LocationDTO created = locationService.create(dto);
        return ResponseEntity.ok(created);
    }

    // Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getById(@PathVariable UUID id) {
        LocationDTO location = locationService.getById(id);
        return ResponseEntity.ok(location);
    }
}


