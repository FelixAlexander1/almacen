package com.empresa.almacen.adapters.inbound.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.empresa.almacen.adapters.outbound.persistence.mapper.PickOrderDTOMapper;
import com.empresa.almacen.application.dto.PickOrderDTO;
import com.empresa.almacen.application.dto.PickingRequestDTO;
import com.empresa.almacen.application.service.PickingService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pickings")
@RequiredArgsConstructor
@Validated
public class PickingController {

    private final PickingService pickingService;
    private final PickOrderDTOMapper pickOrderDTOMapper;

    // Crear una orden de picking
    @PostMapping
    public ResponseEntity<PickOrderDTO> createPickOrder(@Valid @RequestBody PickOrderDTO request) {
        // DTO → Domain
        var pickOrderDomain = pickOrderDTOMapper.toDomain(request);

        // Servicio crea usando dominio
        var created = pickingService.createPickOrder(pickOrderDomain);

        // Domain → DTO
        return ResponseEntity.ok(pickOrderDTOMapper.toDTO(created));
    }

    // Confirmar picking
    @PostMapping("/{pickOrderId}/confirm")
    public ResponseEntity<PickOrderDTO> confirmPicking(
            @PathVariable UUID pickOrderId,
            @Valid @RequestBody PickingRequestDTO request) {
        request.setPickOrderId(pickOrderId);

        var updated = pickingService.pick(request);
        return ResponseEntity.ok(pickOrderDTOMapper.toDTO(updated));
    }

    // Listar órdenes de picking
    @GetMapping
    public ResponseEntity<List<PickOrderDTO>> listAll() {
        var orders = pickingService.findAll();
        return ResponseEntity.ok(
                orders.stream()
                        .map(pickOrderDTOMapper::toDTO)
                        .collect(Collectors.toList())
        );
    }

    // Consultar una orden específica
    @GetMapping("/{id}")
    public ResponseEntity<PickOrderDTO> getById(@PathVariable UUID id) {
        var order = pickingService.findById(id);
        return ResponseEntity.ok(pickOrderDTOMapper.toDTO(order));
    }
}


