package com.empresa.almacen.adapters.inbound.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.empresa.almacen.adapters.outbound.persistence.mapper.InboundDTOMapper;
import com.empresa.almacen.application.dto.InboundDTO;
import com.empresa.almacen.application.dto.ReceiveRequestDTO;
import com.empresa.almacen.application.service.ReceiveInboundService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inbounds")
@RequiredArgsConstructor
@Validated
public class InboundController {

    private final ReceiveInboundService receiveInboundService;
    private final InboundDTOMapper inboundDTOMapper;

    
    @PostMapping
    public ResponseEntity<InboundDTO> createInbound(@Valid @RequestBody InboundDTO request) {
        // Convertir DTO → Domain
        var inboundDomain = inboundDTOMapper.toDomain(request);

        // Llamar servicio con modelo de dominio
        var createdInbound = receiveInboundService.createInbound(inboundDomain);

        // Devolver como DTO
        return ResponseEntity.ok(inboundDTOMapper.toDTO(createdInbound));
    }

    // Recibir productos
    @PostMapping("/{inboundId}/receive")
    public ResponseEntity<InboundDTO> receiveInbound(
            @PathVariable UUID inboundId,
            @Valid @RequestBody ReceiveRequestDTO request) {
        request.setInboundId(inboundId);

        var inbound = receiveInboundService.receive(request);

        return ResponseEntity.ok(inboundDTOMapper.toDTO(inbound));
    }

    // Listar todos los Inbounds
    @GetMapping
    public ResponseEntity<List<InboundDTO>> listAll() {
        var inbounds = receiveInboundService.findAll();
        return ResponseEntity.ok(
                inbounds.stream()
                        .map(inboundDTOMapper::toDTO)
                        .collect(Collectors.toList())
        );
    }

    // Consultar un Inbound específico
    @GetMapping("/{id}")
    public ResponseEntity<InboundDTO> getById(@PathVariable UUID id) {
        var inbound = receiveInboundService.findById(id);
        return ResponseEntity.ok(inboundDTOMapper.toDTO(inbound));
    }
}


