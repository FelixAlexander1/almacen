package com.empresa.almacen.application.mapper;

import com.empresa.almacen.adapters.outbound.persistence.entity.InventoryItemEntity;
import com.empresa.almacen.application.dto.InventoryItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    // Entity -> DTO
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "location.id", target = "locationId")
    InventoryItemDTO toDTO(InventoryItemEntity entity);

    // DTO -> Entity
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "location", ignore = true)
    InventoryItemEntity toEntity(InventoryItemDTO dto);

    
}




