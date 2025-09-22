package com.empresa.almacen.adapters.outbound.persistence.mapper;

import com.empresa.almacen.domain.model.Inbound.InboundItem;
import com.empresa.almacen.adapters.outbound.persistence.entity.InboundItemEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface InboundItemMapper {

    // Mapea InboundItem (domain) a InboundItemEntity (JPA)
    @Mapping(target = "inbound", ignore = true) // se manejará en InboundMapper
    @Mapping(source = "productId", target = "product", qualifiedByName = "mapProductIdToEntity")
    InboundItemEntity toEntity(InboundItem domain);

    // Mapea InboundItemEntity (JPA) a InboundItem (domain)
    @Mapping(source = "product", target = "productId", qualifiedByName = "mapProductEntityToId")
    InboundItem toDomain(InboundItemEntity entity);

    // ===========================
    // Métodos auxiliares MapStruct
    // ===========================

    @Named("mapProductIdToEntity")
    default ProductEntity mapProductIdToEntity(UUID productId) {
        if (productId == null) return null;
        ProductEntity product = new ProductEntity();
        product.setId(productId);
        return product;
    }

    @Named("mapProductEntityToId")
    default UUID mapProductEntityToId(ProductEntity product) {
        return product != null ? product.getId() : null;
    }
}
