package com.empresa.almacen.adapters.outbound.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empresa.almacen.adapters.outbound.persistence.entity.InventoryItemEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.LocationEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.ProductEntity;
import com.empresa.almacen.application.dto.InventoryItemDTO;
import com.empresa.almacen.application.dto.ProductDTO;
import com.empresa.almacen.domain.model.InventoryItem;
import com.empresa.almacen.application.dto.LocationDTO;

@Mapper(componentModel = "spring")
public interface InventoryItemMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "product", target = "product")   
    @Mapping(source = "location", target = "location") 
    InventoryItemDTO toDTO(InventoryItemEntity entity);

    default ProductDTO toProductDTO(ProductEntity entity) {
        if (entity == null) return null;
        return ProductDTO.builder()
                .id(entity.getId())
                .sku(entity.getSku())
                .name(entity.getName())
                .description(entity.getDescription())
                .uom(entity.getUom())
                .length(entity.getLength())
                .width(entity.getWidth())
                .height(entity.getHeight())
                .weight(entity.getWeight())
                .hazardous(entity.isHazardous())
                .build();
    }

    default LocationDTO toLocationDTO(LocationEntity entity) {
        if (entity == null) return null;
        return LocationDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .type(entity.getType())
                .capacity(entity.getCapacity())
                .build();
    }

    default InventoryItem toDomain(InventoryItemEntity entity) {
        if (entity == null) return null;
        return InventoryItem.builder()
                .id(entity.getId())
                .productId(entity.getProduct().getId())
                .locationId(entity.getLocation().getId())
                .quantityTotal(entity.getQuantityTotal())
                .quantityReserved(entity.getQuantityReserved())
                .lotNumber(entity.getLotNumber())
                .expiryDate(entity.getExpiryDate())
                .build();
    }

    default InventoryItemEntity toEntity(InventoryItem domain) {
        if (domain == null) return null;
        InventoryItemEntity entity = new InventoryItemEntity();
        entity.setId(domain.getId());
        entity.setQuantityTotal(domain.getQuantityTotal());
        entity.setQuantityReserved(domain.getQuantityReserved());
        entity.setLotNumber(domain.getLotNumber());
        entity.setExpiryDate(domain.getExpiryDate());
        return entity;
    }

}

