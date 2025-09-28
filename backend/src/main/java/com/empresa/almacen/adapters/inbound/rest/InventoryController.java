package com.empresa.almacen.adapters.inbound.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import javax.validation.Valid;

import com.empresa.almacen.application.dto.InventoryCountDTO;
import com.empresa.almacen.application.dto.InventoryItemDTO;
import com.empresa.almacen.application.dto.InventoryTransferDTO;
import com.empresa.almacen.application.dto.ReserveRequest;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.empresa.almacen.application.service.InventoryItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryItemService inventoryService;

    @PostMapping
    public InventoryItemDTO create(@RequestBody @Valid InventoryItemDTO dto) {
        return inventoryService.create(dto);
    }


    @GetMapping
    public List<InventoryItemDTO> search(@RequestParam(required = false) UUID productId,
                                         @RequestParam(required = false) UUID locationId) {
        return inventoryService.search(productId, locationId);
    }

    @PostMapping("/transfer")
    public void transfer(@RequestBody @Valid InventoryTransferDTO dto) {
        inventoryService.transfer(dto);
    }

    @PostMapping("/count")
    public void count(@RequestBody @Valid InventoryCountDTO dto) {
        inventoryService.count(dto);
    }

    @PostMapping("/adjust")
    public void adjust(@RequestBody @Valid InventoryCountDTO dto) {
        inventoryService.adjust(dto);
    }
    @PostMapping("/reserve")
    public ResponseEntity<?> reserveStock(@RequestBody ReserveRequest request) {
        inventoryService.reserveStock(request.getProductId(), request.getLocationId(), request.getReservedQuantity());
        return ResponseEntity.ok().build();
    }


}


