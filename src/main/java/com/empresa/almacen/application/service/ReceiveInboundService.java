package com.empresa.almacen.application.service;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empresa.almacen.adapters.outbound.persistence.entity.InboundEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.InventoryItemEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.LocationEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.ProductEntity;
import com.empresa.almacen.adapters.outbound.persistence.mapper.InboundMapper;
import com.empresa.almacen.adapters.outbound.persistence.repository.InboundRepository;
import com.empresa.almacen.adapters.outbound.persistence.repository.InventoryItemRepository;
import com.empresa.almacen.adapters.outbound.persistence.repository.LocationJpaRepository;
import com.empresa.almacen.adapters.outbound.persistence.repository.ProductRepository;
import com.empresa.almacen.application.dto.ReceiveRequestDTO;
import com.empresa.almacen.domain.model.Inbound;

@Service
@RequiredArgsConstructor
public class ReceiveInboundService {

    private final InboundRepository inboundRepository;
    private final InventoryItemRepository inventoryRepository;
    private final InboundMapper inboundMapper;
    private final ProductRepository productRepository;
    private final LocationJpaRepository locationRepository;
    private final ActivityService activityService;


    @Transactional
    public Inbound receive(ReceiveRequestDTO request) {
        // Obtener el inbound
        InboundEntity inboundEntity = inboundRepository.findById(request.getInboundId())
                .orElseThrow(() -> new RuntimeException("Inbound no encontrado"));

        // Procesar cada item recibido
        request.getReceivedItems().forEach(receivedItem -> {
            LocationEntity locationEntity = locationRepository.findById(receivedItem.getLocationId())
                    .orElseThrow(() -> new IllegalArgumentException("Ubicación no encontrada"));

            ProductEntity productEntity = productRepository.findById(receivedItem.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

            // Buscar inventario existente o crear uno nuevo
            InventoryItemEntity inventoryEntity = inventoryRepository
                    .findByProductIdAndLocationId(productEntity.getId(), locationEntity.getId())
                    .orElseGet(() -> InventoryItemEntity.builder()
                            .product(productEntity)
                            .location(locationEntity)
                            .quantityTotal(0)
                            .quantityReserved(0)
                            .lotNumber(receivedItem.getLot())
                            .expiryDate(null)
                            .build()
                    );

            // Sumar la cantidad recibida
            inventoryEntity.setQuantityTotal(inventoryEntity.getQuantityTotal() + receivedItem.getReceivedQty());
            inventoryEntity.setLotNumber(receivedItem.getLot());

            inventoryRepository.save(inventoryEntity);

            // Actualizar la ubicación del item en el inbound
            inboundEntity.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(productEntity.getId()))
                    .forEach(item -> item.setLocation(locationEntity));
        });

        // Cambiar estado a RECEIVED
        inboundEntity.setStatus(InboundEntity.InboundStatus.RECEIVED);
        inboundRepository.save(inboundEntity);

        // Devolver como dominio
        return inboundMapper.toDomain(inboundEntity);
    }



    public Inbound createInbound(Inbound request) {
        request.setId(null); // asegurarse que el ID sea nulo para creación
        request.setStatus(Inbound.InboundStatus.CREATED);

        // Mapear a entity
        InboundEntity entity = inboundMapper.toEntity(request);

        // Asegurar que cada item tenga product y inbound
        if (entity.getItems() != null) {
            entity.getItems().forEach(item -> {
                item.setInbound(entity);
            });
        }

        // Guardar
        InboundEntity savedEntity = inboundRepository.save(entity);

        activityService.logActivity("Inbound #" + savedEntity.getAsnNumber() + " creado" );

        return inboundMapper.toDomain(savedEntity);
    }


    public List<Inbound> findAll() {
        var entities = inboundRepository.findAll();
        return entities.stream()
                .map(inboundMapper::toDomain)
                .toList();
    }
    public Inbound findById(UUID id) {
        var entity = inboundRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inbound no encontrado"));
        return inboundMapper.toDomain(entity);
    }
}
