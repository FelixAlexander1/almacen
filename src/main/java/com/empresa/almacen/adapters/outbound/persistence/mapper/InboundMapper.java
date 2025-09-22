package com.empresa.almacen.adapters.outbound.persistence.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.empresa.almacen.adapters.outbound.persistence.entity.InboundEntity;
import com.empresa.almacen.domain.model.Inbound;

@Mapper(componentModel = "spring", uses = {InboundItemMapper.class})
public interface InboundMapper {

    Inbound toDomain(InboundEntity entity);

    InboundEntity toEntity(Inbound domain);

    @AfterMapping
    default void linkItems(@MappingTarget InboundEntity inboundEntity) {
        if (inboundEntity.getItems() != null) {
            inboundEntity.getItems().forEach(item -> item.setInbound(inboundEntity));
        }
    }
}

