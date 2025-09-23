package com.empresa.almacen.adapters.outbound.persistence.mapper;

import org.mapstruct.Mapper;
import com.empresa.almacen.adapters.outbound.persistence.entity.PickOrderEntity;
import com.empresa.almacen.domain.model.PickOrder;

@Mapper(componentModel = "spring", uses = {PickOrderLineMapper.class})
public interface PickOrderMapper {

    PickOrder toDomain(PickOrderEntity entity);
    PickOrderEntity toEntity(PickOrder domain);

}
