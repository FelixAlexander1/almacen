package com.empresa.almacen.adapters.outbound.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.empresa.almacen.adapters.outbound.persistence.entity.LocationEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.ProductEntity;
import com.empresa.almacen.domain.model.Location;
import com.empresa.almacen.domain.model.Product;


@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toDomain(ProductEntity entity);
    ProductEntity toEntity(Product domain);

    // 👇 Para convertir entre dominio y persistencia
    LocationEntity locationToEntity(Location location);
    Location locationToDomain(LocationEntity entity);
}

