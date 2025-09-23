package com.empresa.almacen.adapters.outbound.persistence.mapper;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.empresa.almacen.adapters.outbound.persistence.entity.LocationEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.PickOrderEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.PickOrderLineEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.ProductEntity;
import com.empresa.almacen.domain.model.PickOrder;

@Mapper(componentModel = "spring")
public interface PickOrderLineMapper {

    PickOrderLineMapper INSTANCE = Mappers.getMapper(PickOrderLineMapper.class);

    @Mapping(source = "product", target = "productId", qualifiedByName = "mapProductToId")
    @Mapping(source = "location", target = "locationId", qualifiedByName = "mapLocationToId")
    PickOrder.PickOrderLine toDomain(PickOrderLineEntity entity);

    // Mapea el dominio a entidad asegurando LocationEntity persistida
    default PickOrderLineEntity toEntity(
        PickOrder.PickOrderLine domain,
        PickOrderEntity pickOrder,
        LocationEntity location
    ) {
        if (domain == null) return null;

        PickOrderLineEntity entity = new PickOrderLineEntity();
        entity.setQty(domain.getQty());
        entity.setPickedQty(domain.getPickedQty());
        entity.setPickOrder(pickOrder);

        // Producto
        if (domain.getProductId() != null) {
            ProductEntity product = new ProductEntity();
            product.setId(domain.getProductId());
            entity.setProduct(product);
        }

        // Ubicación cargada desde DB
        entity.setLocation(location);

        return entity;
    }


    @org.mapstruct.Named("mapProductToId")
    default UUID mapProductToId(ProductEntity product) {
        return product == null ? null : product.getId();
    }

    @org.mapstruct.Named("mapLocationToId")
    default UUID mapLocationToId(LocationEntity location) {
        return location == null ? null : location.getId();
    }
}
