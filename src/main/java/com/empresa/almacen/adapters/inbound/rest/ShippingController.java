package com.empresa.almacen.adapters.inbound.rest;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.empresa.almacen.adapters.outbound.persistence.entity.ShipmentEntity;
import com.empresa.almacen.adapters.outbound.persistence.mapper.ShipmentDTOMapper;
import com.empresa.almacen.application.dto.ShipmentDTO;
import com.empresa.almacen.application.dto.ShipmentRequestDTO;
import com.empresa.almacen.application.service.ShippingService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor
@Validated
public class ShippingController {

    private final ShipmentDTOMapper shipmentDTOMapper;
    private final ShippingService shipmentService;

    // Crear envío
    @PostMapping
    public ResponseEntity<ShipmentEntity> createShipment(@RequestBody ShipmentRequestDTO request) {
        ShipmentEntity created = shipmentService.createShipment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Confirmar salida del envío
     @PostMapping("/{shipmentId}/ship")
    public ResponseEntity<ShipmentDTO> confirmShipment(@PathVariable UUID shipmentId) {
        ShipmentDTO dto = shipmentService.confirmShipment(shipmentId);
        return ResponseEntity.ok(dto);
    }

    // Listar envíos
    @GetMapping
    public ResponseEntity<List<ShipmentDTO>> listAll() {
        var shipments = shipmentService.findAll();
        return ResponseEntity.ok(
                shipments.stream()
                        .map(shipmentDTOMapper::toDTO)
                        .collect(Collectors.toList())
        );
    }

    // Consultar envío específico
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentDTO> getById(@PathVariable UUID id) {
        var shipment = shipmentService.findById(id);
        return ResponseEntity.ok(shipmentDTOMapper.toDTO(shipment));
    }
}

