package com.empresa.almacen.adapters.outbound.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.empresa.almacen.adapters.outbound.persistence.entity.ShipmentEntity;
import com.empresa.almacen.domain.model.Shipment;


@Mapper(componentModel = "spring", uses = {ShipmentItemMapper.class})
public interface ShipmentMapper {
ShipmentMapper INSTANCE = Mappers.getMapper(ShipmentMapper.class);


Shipment toDomain(ShipmentEntity entity);
ShipmentEntity toEntity(Shipment domain);
}
