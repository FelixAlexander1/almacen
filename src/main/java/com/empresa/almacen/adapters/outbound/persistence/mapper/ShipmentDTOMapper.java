package com.empresa.almacen.adapters.outbound.persistence.mapper;

import com.empresa.almacen.application.dto.ShipmentDTO;
import com.empresa.almacen.domain.model.Shipment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShipmentDTOMapper {
    ShipmentDTO toDTO(Shipment domain);
    Shipment toDomain(ShipmentDTO dto);
}
