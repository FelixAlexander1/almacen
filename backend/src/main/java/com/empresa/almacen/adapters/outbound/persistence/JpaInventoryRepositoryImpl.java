package com.empresa.almacen.adapters.outbound.persistence;

import com.empresa.almacen.adapters.outbound.persistence.entity.InventoryItemEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.LocationEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.ProductEntity;
import com.empresa.almacen.adapters.outbound.persistence.mapper.InventoryItemMapper;
import com.empresa.almacen.adapters.outbound.persistence.repository.InventoryItemRepository;
import com.empresa.almacen.adapters.outbound.persistence.repository.LocationJpaRepository;
import com.empresa.almacen.adapters.outbound.persistence.repository.ProductRepository;
import com.empresa.almacen.domain.model.InventoryItem;
import com.empresa.almacen.domain.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaInventoryRepositoryImpl implements InventoryRepository {

    private final InventoryItemRepository jpaRepository;
    private final LocationJpaRepository locationJpaRepository;
    private final InventoryItemRepository inventoryJpaRepository;
    private final ProductRepository productJpaRepository;
    private final InventoryItemMapper inventoryItemMapper;

    

    @Override
    public List<InventoryItem> findByFilters(UUID productId, UUID locationId) {
        // Si no se pasa filtro, se devuelve todo
        if (productId == null && locationId == null) {
            return jpaRepository.findAll()
                    .stream()
                    .map(inventoryItemMapper::toDomain)
                    .collect(Collectors.toList());
        }

        if (productId != null && locationId != null) {
            return jpaRepository.findByProductIdAndLocationId(productId, locationId)
                    .map(List::of)
                    .orElse(List.of())
                    .stream()
                    .map(inventoryItemMapper::toDomain)
                    .collect(Collectors.toList());
        }

        if (productId != null) {
            return jpaRepository.findByProductId(productId)
                    .stream()
                    .map(inventoryItemMapper::toDomain)
                    .collect(Collectors.toList());
        }

        return jpaRepository.findByLocationId(locationId)
                .stream()
                .map(inventoryItemMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<InventoryItem> findByProductAndLocation(UUID productId, UUID locationId) {
        return jpaRepository.findByProductIdAndLocationId(productId, locationId)
                .map(inventoryItemMapper::toDomain);
    }

    @Override
    public InventoryItem save(InventoryItem domainItem) {
        // Convertir de dominio a entidad (ignora product y location)
        InventoryItemEntity entity = inventoryItemMapper.toEntity(domainItem);

        LocationEntity locationEntity = locationJpaRepository.findById(domainItem.getLocationId())
                .orElseThrow(() -> new IllegalArgumentException("Ubicación no encontrada"));
        entity.setLocation(locationEntity);

        ProductEntity productEntity = productJpaRepository.findById(domainItem.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        entity.setProduct(productEntity);

        InventoryItemEntity saved = inventoryJpaRepository.save(entity);

        return inventoryItemMapper.toDomain(saved);

    }


    @Override
    public void delete(InventoryItem item) {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Optional<InventoryItem> findById(UUID id) {
        // TODO Auto-generated method stubon("Unimplemented method 'findById'");
    }


}

