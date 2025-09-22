package com.empresa.almacen.adapters.inbound.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.empresa.almacen.application.dto.CrossDockOperationDTO;
import com.empresa.almacen.application.service.CrossDockService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/crossdock")
@RequiredArgsConstructor
@Validated
public class CrossDockController {

    private final CrossDockService crossDockService;

    @PostMapping
    public ResponseEntity<CrossDockOperationDTO> create(@Valid @RequestBody CrossDockOperationDTO request) {
        return ResponseEntity.ok(crossDockService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<CrossDockOperationDTO>> listAll() {
        return ResponseEntity.ok(crossDockService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CrossDockOperationDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(crossDockService.findById(id));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<CrossDockOperationDTO> complete(@PathVariable UUID id) {
        return ResponseEntity.ok(crossDockService.complete(id));
    }
}
