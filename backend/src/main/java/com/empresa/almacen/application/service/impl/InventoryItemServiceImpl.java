package com.empresa.almacen.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.empresa.almacen.application.service.InventoryItemService;
import com.empresa.almacen.domain.model.PickOrder;
import com.empresa.almacen.application.dto.InventoryCountDTO;
import com.empresa.almacen.application.dto.InventoryItemDTO;
import com.empresa.almacen.application.dto.InventoryTransferDTO;
import com.empresa.almacen.adapters.outbound.persistence.entity.InventoryItemEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.LocationEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.PickOrderEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.PickOrderLineEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.ProductEntity;
import com.empresa.almacen.adapters.outbound.persistence.repository.InventoryItemRepository;
import com.empresa.almacen.adapters.outbound.persistence.repository.LocationJpaRepository;
import com.empresa.almacen.adapters.outbound.persistence.repository.PickOrderLineRepository;
import com.empresa.almacen.adapters.outbound.persistence.repository.ProductRepository;
import com.empresa.almacen.application.mapper.InventoryMapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryItemServiceImpl implements InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;
    private final ProductRepository productRepository;
    private final LocationJpaRepository locationRepository;
    private final InventoryMapper inventoryMapper;
    private final PickOrderLineRepository pickOrderLineRepository;

    @Override
        public InventoryItemDTO create(InventoryItemDTO dto) {
        // Buscar el producto
        ProductEntity product = productRepository.findById(dto.getProduct().getId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        // Buscar la ubicación
        LocationEntity receivingLocation = locationRepository.findById(dto.getLocation().getId())
                .orElseThrow(() -> new IllegalArgumentException("Ubicación no encontrada"));

        // Buscar si ya existe el inventario en esa ubicación
        InventoryItemEntity item = inventoryItemRepository
                .findByProductIdAndLocationId(product.getId(), receivingLocation.getId())
                .orElseGet(() -> InventoryItemEntity.builder()
                        .product(product) // usar el producto cargado
                        .location(receivingLocation) 
                        .quantityTotal(0)
                        .quantityReserved(0)
                        .lotNumber(dto.getLotNumber())
                        .expiryDate(null)
                        .build()
                );

        // Actualizar cantidades
        item.setQuantityTotal(item.getQuantityTotal() + dto.getQuantityTotal());

        // Guardar en la base de datos
        inventoryItemRepository.save(item);

        return inventoryMapper.toDTO(item);
        }



    @Override
        @Transactional(readOnly = true)
        public List<InventoryItemDTO> search(UUID productId, UUID locationId) {
        List<InventoryItemEntity> entities;

        if (productId != null && locationId != null) {
                entities = inventoryItemRepository.findByProductIdAndLocationId(productId, locationId)
                        .map(List::of)
                        .orElse(List.of());
        } else if (productId != null) {
                entities = inventoryItemRepository.findByProductId(productId);
        } else if (locationId != null) {
                entities = inventoryItemRepository.findByLocationId(locationId);
        } else {
                entities = inventoryItemRepository.findAll();
        }

        // Convertimos cada entidad a DTO y calculamos el stock reservado
        return entities.stream()
                .map(entity -> {
                        InventoryItemDTO dto = inventoryMapper.toDTO(entity);
                        int reserved = calculateReserved(entity.getProduct(), entity.getLocation());
                        dto.setQuantityReserved(reserved);
                        return dto;
                })
                .collect(Collectors.toList());
        }


    @Override
    public void transfer(InventoryTransferDTO dto) {
        if (dto.getFromLocationId().equals(dto.getToLocationId())) {
            throw new IllegalArgumentException("La ubicación origen y destino no pueden ser la misma");
        }

        ProductEntity product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        LocationEntity fromLocation = locationRepository.findById(dto.getFromLocationId())
                .orElseThrow(() -> new IllegalArgumentException("Ubicación origen no encontrada"));

        LocationEntity toLocation = locationRepository.findById(dto.getToLocationId())
                .orElseThrow(() -> new IllegalArgumentException("Ubicación destino no encontrada"));

        InventoryItemEntity source = inventoryItemRepository
                .findByProductIdAndLocationId(product.getId(), fromLocation.getId())
                .orElseThrow(() -> new IllegalArgumentException("No hay stock en la ubicación origen"));

        if (source.getQuantityTotal() < dto.getQuantity()) {
            throw new IllegalArgumentException("Stock insuficiente en la ubicación origen");
        }

        source.setQuantityTotal(source.getQuantityTotal() - dto.getQuantity());
        inventoryItemRepository.save(source);

        InventoryItemEntity target = inventoryItemRepository
                .findByProductIdAndLocationId(product.getId(), toLocation.getId())
                .orElseGet(() -> InventoryItemEntity.builder()
                        .product(product)
                        .location(toLocation)
                        .quantityTotal(0)
                        .quantityReserved(0)
                        .lotNumber(null)
                        .expiryDate(null)
                        .build()
                );

        target.setQuantityTotal(target.getQuantityTotal() + dto.getQuantity());
        inventoryItemRepository.save(target);
    }

    @Override
    public void count(InventoryCountDTO dto) {
        ProductEntity product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        LocationEntity location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new IllegalArgumentException("Ubicación no encontrada"));

        InventoryItemEntity item = inventoryItemRepository
                .findByProductIdAndLocationId(product.getId(), location.getId())
                .orElseGet(() -> InventoryItemEntity.builder()
                        .product(product)
                        .location(location)
                        .quantityTotal(0)
                        .quantityReserved(0)
                        .lotNumber(dto.getLotNumber())
                        .expiryDate(null)
                        .build()
                );

        item.setQuantityTotal(dto.getCountedQuantity());
        item.setLotNumber(dto.getLotNumber());

        inventoryItemRepository.save(item);
    }

        @Override
        public void adjust(InventoryCountDTO dto) {
        ProductEntity product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        LocationEntity location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new IllegalArgumentException("Ubicación no encontrada"));

        InventoryItemEntity item = inventoryItemRepository
                .findByProductIdAndLocationId(product.getId(), location.getId())
                .orElseThrow(() -> new IllegalArgumentException("Inventario no encontrado para ese producto y ubicación"));

        // Actualizar el stock
        item.setQuantityTotal(dto.getCountedQuantity());

        // Opcional: actualizar número de lote
        if (dto.getLotNumber() != null) {
                item.setLotNumber(dto.getLotNumber());
        }

        inventoryItemRepository.save(item);
        }

    public int calculateReserved(ProductEntity product, LocationEntity location) {
    // Convertir el enum del dominio al enum de la entidad
        PickOrderEntity.PickOrderStatus entityStatus =
                PickOrderEntity.PickOrderStatus.CREATED;

        // Traer todas las líneas pendientes que correspondan al producto y ubicación
        List<PickOrderLineEntity> pendingLines = pickOrderLineRepository
                .findByProductAndLocationAndPickOrderStatus(product, location, entityStatus);

        // Sumar la cantidad pendiente (qty - pickedQty)
        return pendingLines.stream()
                .mapToInt(line -> line.getQty() - line.getPickedQty())
                .sum();
        }






}
