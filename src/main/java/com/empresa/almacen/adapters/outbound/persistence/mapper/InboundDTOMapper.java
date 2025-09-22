package com.empresa.almacen.adapters.outbound.persistence.mapper;

import com.empresa.almacen.application.dto.InboundDTO;
import com.empresa.almacen.domain.model.Inbound;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InboundDTOMapper {
    InboundDTO toDTO(Inbound inbound);
    Inbound toDomain(InboundDTO dto);
}

