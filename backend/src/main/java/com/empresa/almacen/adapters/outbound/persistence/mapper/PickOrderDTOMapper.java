package com.empresa.almacen.adapters.outbound.persistence.mapper;

import org.mapstruct.Mapper;

import com.empresa.almacen.application.dto.PickOrderDTO;
import com.empresa.almacen.domain.model.PickOrder;

@Mapper(componentModel = "spring")
public interface PickOrderDTOMapper {

    PickOrderDTO toDTO(PickOrder domain);
    PickOrder toDomain(PickOrderDTO dto);

}
