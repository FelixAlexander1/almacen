package com.empresa.almacen.adapters.outbound.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import com.empresa.almacen.adapters.outbound.persistence.entity.CrossDockOperationEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.InboundEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.ShipmentEntity;
import com.empresa.almacen.domain.model.CrossDockOperation;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CrossDockMapper {

    @Mapping(source = "inbound", target = "inboundId", qualifiedByName = "mapInboundToId")
    @Mapping(source = "outShipment", target = "outShipmentId", qualifiedByName = "mapOutShipmentToId")
    CrossDockOperation toDomain(CrossDockOperationEntity entity);

    default CrossDockOperationEntity toEntity(CrossDockOperation domain) {
        if (domain == null) return null;

        CrossDockOperationEntity entity = new CrossDockOperationEntity();

        // Placeholder para Inbound
        if (domain.getInboundId() != null) {
            InboundEntity inbound = new InboundEntity();
            inbound.setId(domain.getInboundId());
            entity.setInbound(inbound);
        }

        // Placeholder para OutShipment
        if (domain.getOutShipmentId() != null) {
            ShipmentEntity outShipment = new ShipmentEntity();
            outShipment.setId(domain.getOutShipmentId());
            entity.setOutShipment(outShipment);
        }

        return entity;
    }

    // Helpers para MapStruct
    @Named("mapInboundToId")
    default UUID mapInboundToId(InboundEntity inbound) {
        return inbound == null ? null : inbound.getId();
    }

    @Named("mapOutShipmentToId")
    default UUID mapOutShipmentToId(ShipmentEntity outShipment) {
        return outShipment == null ? null : outShipment.getId();
    }
}
