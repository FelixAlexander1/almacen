package com.empresa.almacen.application.service;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empresa.almacen.adapters.outbound.persistence.entity.InventoryItemEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.PickOrderEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.ProductEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.ShipmentEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.ShipmentItemEntity;
import com.empresa.almacen.adapters.outbound.persistence.mapper.PickOrderMapper;
import com.empresa.almacen.adapters.outbound.persistence.mapper.ShipmentMapper;
import com.empresa.almacen.adapters.outbound.persistence.repository.InventoryItemRepository;
import com.empresa.almacen.adapters.outbound.persistence.repository.PickOrderRepository;
import com.empresa.almacen.adapters.outbound.persistence.repository.ProductRepository;
import com.empresa.almacen.adapters.outbound.persistence.repository.ShipmentRepository;
import com.empresa.almacen.application.dto.ShipmentDTO;
import com.empresa.almacen.application.dto.ShipmentRequestDTO;
import com.empresa.almacen.application.dto.ShipmentRequestDTO.ShipmentItemDTO;
import com.empresa.almacen.application.dto.ShippingRequestDTO;
import com.empresa.almacen.domain.model.Shipment;

import jakarta.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class ShippingService {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper shipmentMapper;
    private final ProductRepository productRepository;
    private final InventoryItemRepository inventoryRepository;
    private final PickOrderRepository pickOrderRepository;
    private final PickOrderMapper pickOrderMapper;

    @Transactional
    public Shipment ship(ShippingRequestDTO request) {
        var shipmentEntity = shipmentRepository.findById(request.getShipmentId())
                .orElseThrow(() -> new RuntimeException("Shipment no encontrado"));

        Shipment shipment = shipmentMapper.toDomain(shipmentEntity);

        shipment.setStatus(Shipment.ShipmentStatus.IN_TRANSIT);
        shipmentRepository.save(shipmentMapper.toEntity(shipment));

        return shipment;
    }
    @Transactional
    public ShipmentEntity createShipment(ShipmentRequestDTO request) {
        // Crear el Shipment
        ShipmentEntity shipment = new ShipmentEntity();
        shipment.setCarrier(request.getCarrier());
        shipment.setTrackingNumber(request.getTrackingNumber());
        shipment.setStatus(ShipmentEntity.ShipmentStatus.CREATED);

        shipment.setShipmentItems(new ArrayList<>());
        shipment.setPickOrders(new ArrayList<>());

        // Asociar ShipmentItems
        for (ShipmentRequestDTO.ShipmentItemDTO dto : request.getShipmentItems()) {
            ProductEntity product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + dto.getProductId()));

            ShipmentItemEntity item = new ShipmentItemEntity();
            item.setProduct(product);
            item.setQty(dto.getQty());
            item.setShipment(shipment);

            shipment.getShipmentItems().add(item);
        }

        // Asociar PickOrders seleccionadas
        for (UUID pickOrderId : request.getPickOrderIds()) {
            PickOrderEntity pickOrder = pickOrderRepository.findById(pickOrderId)
                .orElseThrow(() -> new RuntimeException("PickOrder no encontrada: " + pickOrderId));

            pickOrder.setShipment(shipment); // vínculo bidireccional
            shipment.getPickOrders().add(pickOrder);
        }

        return shipmentRepository.save(shipment);
    }


    @Transactional(readOnly = true)
    public Shipment findById(UUID id) {
        var entity = shipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment no encontrado"));
        return shipmentMapper.toDomain(entity);
    }

    @Transactional(readOnly = true)
    public List<Shipment> findAll() {
        var entities = shipmentRepository.findAll();
        return entities.stream()
                .map(shipmentMapper::toDomain)
                .toList();
    }

    @Transactional
public ShipmentDTO confirmShipment(UUID shipmentId) {
    ShipmentEntity shipment = shipmentRepository.findById(shipmentId)
        .orElseThrow(() -> new EntityNotFoundException("Shipment not found"));

    if (shipment.getStatus() != ShipmentEntity.ShipmentStatus.CREATED) {
        throw new IllegalStateException("Shipment already confirmed or invalid state");
    }

    // Restar stock
    for (ShipmentItemEntity item : shipment.getShipmentItems()) {
        ProductEntity product = item.getProduct();
        List<InventoryItemEntity> inventoryList = inventoryRepository.findByProductId(product.getId());
        if (inventoryList.isEmpty()) throw new IllegalStateException("No inventory for product " + product.getSku());

        int remainingQty = item.getQty();
        for (InventoryItemEntity inventory : inventoryList) {
            int stock = inventory.getQuantityTotal();
            int reserved = inventory.getQuantityReserved();

            if (stock >= remainingQty) {
                inventory.setQuantityTotal(stock - remainingQty);
                inventory.setQuantityReserved(Math.max(0, reserved - remainingQty));
                inventoryRepository.save(inventory);
                remainingQty = 0;
                break;
            } else {
                inventory.setQuantityTotal(0);
                inventory.setQuantityReserved(Math.max(0, reserved - stock));
                inventoryRepository.save(inventory);
                remainingQty -= stock;
            }
        }

        if (remainingQty > 0) throw new IllegalStateException("Not enough stock for product " + product.getSku());
    }

    // Cambiar estado del shipment
    shipment.setStatus(ShipmentEntity.ShipmentStatus.IN_TRANSIT);

    // Actualizar PickOrders asociadas
    for (PickOrderEntity po : shipment.getPickOrders()) {
        po.setStatus(PickOrderEntity.PickOrderStatus.COMPLETED);
        pickOrderRepository.save(po); // asegurar persistencia
    }

    shipmentRepository.save(shipment);
    return mapToDTO(shipment);
}

private ShipmentDTO mapToDTO(ShipmentEntity entity) {
    ShipmentDTO dto = new ShipmentDTO();
    dto.setId(entity.getId());
    dto.setCarrier(entity.getCarrier());
    dto.setTrackingNumber(entity.getTrackingNumber());
    dto.setStatus(entity.getStatus().name());

    List<ShipmentDTO.ShipmentItemDTO> items = entity.getShipmentItems().stream().map(item -> {
        ShipmentDTO.ShipmentItemDTO itemDTO = new ShipmentDTO.ShipmentItemDTO();
        itemDTO.setProductId(item.getProduct().getId());
        itemDTO.setQty(item.getQty());
        return itemDTO;
    }).collect(Collectors.toList());

    dto.setShipmentItems(items);
    return dto;
}


}
