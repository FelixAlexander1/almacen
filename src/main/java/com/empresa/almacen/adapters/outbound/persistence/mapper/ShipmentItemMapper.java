package com.empresa.almacen.adapters.outbound.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.empresa.almacen.adapters.outbound.persistence.entity.ShipmentItemEntity;
import com.empresa.almacen.domain.model.Shipment.ShipmentItem;


@Mapper(componentModel = "spring")
public interface ShipmentItemMapper {
ShipmentItemMapper INSTANCE = Mappers.getMapper(ShipmentItemMapper.class);


@Mapping(source = "product.id", target = "productId")
ShipmentItem toDomain(ShipmentItemEntity entity);


@Mapping(source = "productId", target = "product.id")
ShipmentItemEntity toEntity(ShipmentItem domain);
}
