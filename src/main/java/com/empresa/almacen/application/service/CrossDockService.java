package com.empresa.almacen.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empresa.almacen.adapters.outbound.persistence.entity.CrossDockOperationEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.InboundEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.ShipmentEntity;
import com.empresa.almacen.adapters.outbound.persistence.repository.CrossDockOperationRepository;
import com.empresa.almacen.adapters.outbound.persistence.repository.InboundRepository;
import com.empresa.almacen.adapters.outbound.persistence.repository.ShipmentRepository;
import com.empresa.almacen.application.dto.CrossDockOperationDTO;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrossDockService {

    private final CrossDockOperationRepository crossDockRepository;
    private final InboundRepository inboundRepository;
    private final ShipmentRepository shipmentRepository;

    /**
     * Crea una operación de crossdock
     */
    @Transactional
    public CrossDockOperationDTO create(CrossDockOperationDTO request) {
        // Validar inbound
        InboundEntity inbound = inboundRepository.findById(request.getInboundId())
                .orElseThrow(() -> new RuntimeException("Inbound no encontrado"));

        // Validar shipment
        ShipmentEntity shipment = shipmentRepository.findById(request.getOutShipmentId())
                .orElseThrow(() -> new RuntimeException("Shipment no encontrado"));

        CrossDockOperationEntity entity = CrossDockOperationEntity.builder()
                .inbound(inbound)
                .outShipment(shipment)
                .status(CrossDockOperationEntity.CrossDockStatus.CREATED)
                .build();

        crossDockRepository.save(entity);


        return mapToDTO(entity);
    }

    /**
     * Lista todas las operaciones
     */
    public List<CrossDockOperationDTO> findAll() {
        return crossDockRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca una operación por ID
     */
    public CrossDockOperationDTO findById(UUID id) {
        return crossDockRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("CrossDock no encontrado"));
    }

    /**
     * Completa una operación de crossdock
     */
    @Transactional
    public CrossDockOperationDTO complete(UUID id) {
        // 1️⃣ Cargar la operación con lock para evitar conflictos concurrentes
        CrossDockOperationEntity entity = crossDockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CrossDock no encontrado"));

        if (entity.getStatus() == CrossDockOperationEntity.CrossDockStatus.COMPLETED) {
            throw new RuntimeException("Operación ya completada");
        }

        // 2️⃣ Cambiar estado de la operación
        entity.setStatus(CrossDockOperationEntity.CrossDockStatus.COMPLETED);

        // 3️⃣ Actualizar entidades relacionadas
        InboundEntity inbound = entity.getInbound();
        inbound.setStatus(InboundEntity.InboundStatus.RECEIVED);
        inboundRepository.save(inbound);  // Persistimos cambios para evitar detached

        ShipmentEntity shipment = entity.getOutShipment();
        shipment.setStatus(ShipmentEntity.ShipmentStatus.IN_TRANSIT);
        shipmentRepository.save(shipment);  // Persistimos cambios para evitar detached

        // 4️⃣ Guardar operación de crossdock
        crossDockRepository.save(entity);

        // 5️⃣ Mapear a DTO
        return mapToDTO(entity);
    }


    /**
     * Helper para convertir a DTO
     */
    private CrossDockOperationDTO mapToDTO(CrossDockOperationEntity entity) {
        return CrossDockOperationDTO.builder()
                .id(entity.getId())
                .inboundId(entity.getInbound().getId())
                .outShipmentId(entity.getOutShipment().getId())
                .status(entity.getStatus().name())
                .build();
    }
}

