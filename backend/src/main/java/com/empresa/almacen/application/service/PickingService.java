package com.empresa.almacen.application.service;

import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empresa.almacen.adapters.outbound.persistence.entity.LocationEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.PickOrderEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.PickOrderLineEntity;
import com.empresa.almacen.adapters.outbound.persistence.mapper.PickOrderLineMapper;
import com.empresa.almacen.adapters.outbound.persistence.mapper.PickOrderMapper;
import com.empresa.almacen.adapters.outbound.persistence.repository.InventoryItemRepository;
import com.empresa.almacen.adapters.outbound.persistence.repository.LocationJpaRepository;
import com.empresa.almacen.adapters.outbound.persistence.repository.PickOrderRepository;
import com.empresa.almacen.application.dto.PickingRequestDTO;
import com.empresa.almacen.domain.model.PickOrder;

@Service
@RequiredArgsConstructor
public class PickingService {

    private final PickOrderRepository pickOrderRepository;
    private final InventoryItemRepository inventoryRepository;
    private final PickOrderMapper pickOrderMapper;
    private final PickOrderLineMapper pickOrderLineMapper;
    private final LocationJpaRepository locationRepository;
    private final ActivityService activityService;


@Transactional
public PickOrder pick(PickingRequestDTO request) {
    // Obtener PickOrderEntity de DB
    PickOrderEntity pickOrderEntity = pickOrderRepository.findById(request.getPickOrderId())
            .orElseThrow(() -> new RuntimeException("PickOrder no encontrado"));

    // Convertir a dominio para manipular líneas
    PickOrder pickOrder = pickOrderMapper.toDomain(pickOrderEntity);

    // Actualizar inventario y pickedQty
    request.getPickedLines().forEach(line -> {
        var inventoryEntity = inventoryRepository
                .findByProductIdAndLocationId(line.getProductId(), line.getLocationId())
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));

        if (inventoryEntity.getQuantityTotal() < line.getQty()) {
            throw new RuntimeException("Stock insuficiente para el producto: " + line.getProductId());
        }

        // Actualizar inventario
        inventoryEntity.setQuantityTotal(inventoryEntity.getQuantityTotal() - line.getQty());
        inventoryEntity.setQuantityReserved(inventoryEntity.getQuantityReserved() - line.getQty());
        inventoryRepository.save(inventoryEntity);

        // Actualizar cantidad recogida en el dominio
        pickOrder.getLines().stream()
                .filter(l -> l.getProductId().equals(line.getProductId()))
                .findFirst()
                .ifPresent(l -> l.setPickedQty(line.getQty()));
    });

    // Mapear líneas a entidades con PickOrderEntity padre y LocationEntity cargada
    List<PickOrderLineEntity> lineEntities = pickOrder.getLines().stream()
            .<PickOrderLineEntity>map(line -> {
                LocationEntity location = locationRepository.findById(line.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));
                return pickOrderLineMapper.toEntity(line, pickOrderEntity, location);
            })
            .collect(Collectors.toList());



    // Asignar las líneas y actualizar el estado
    pickOrderEntity.getLines().clear();      
    pickOrderEntity.getLines().addAll(lineEntities);  

    pickOrderEntity.setStatus(PickOrderEntity.PickOrderStatus.PICKED);

    // Guardar en DB
    pickOrderRepository.save(pickOrderEntity);

    // Actualizar el estado en el dominio
    pickOrder.setStatus(PickOrder.PickOrderStatus.PICKED);

    return pickOrder;
}



@Transactional
public PickOrder createPickOrder(PickOrder request) {
    // Inicializar datos
    request.setId(null); 
    request.setStatus(PickOrder.PickOrderStatus.CREATED);

    // Mapear la cabecera
    PickOrderEntity entity = pickOrderMapper.toEntity(request);

    // Mapear las líneas y actualizar reservas
    List<PickOrderLineEntity> lineEntities = request.getLines().stream()
        .map(line -> {
            // Cargar la ubicación desde la DB
            LocationEntity location = locationRepository.findById(line.getLocationId())
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada: " + line.getLocationId()));

            // Actualizar reservas
            var inventoryEntity = inventoryRepository
                .findByProductIdAndLocationId(line.getProductId(), line.getLocationId())
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));

            if (inventoryEntity.getQuantityTotal() < line.getQty()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + line.getProductId());
            }

            // Incrementar reservado
            inventoryEntity.setQuantityReserved(inventoryEntity.getQuantityReserved() + line.getQty());
            inventoryRepository.save(inventoryEntity);

            // Pasar la LocationEntity al mapper
            return pickOrderLineMapper.toEntity(line, entity, location);
        })
        .collect(Collectors.toList());

    entity.getLines().clear();
    entity.getLines().addAll(lineEntities);

    // 4Guardar en DB
    PickOrderEntity saved = pickOrderRepository.save(entity);

     activityService.logActivity("Pick Order #" + saved.getId() + " creada");

    // Retornar como dominio
    return pickOrderMapper.toDomain(saved);
}



    public java.util.List<PickOrder> findAll() {
        var entities = pickOrderRepository.findAll();
        return entities.stream()
                .map(pickOrderMapper::toDomain)
                .toList();
    }
    public PickOrder findById(UUID id) {
        var entity = pickOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PickOrder no encontrado"));
        return pickOrderMapper.toDomain(entity);
    }

}
