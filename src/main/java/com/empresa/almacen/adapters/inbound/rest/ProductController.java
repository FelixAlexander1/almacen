package com.empresa.almacen.adapters.inbound.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.empresa.almacen.adapters.outbound.persistence.entity.InventoryItemEntity;
import com.empresa.almacen.adapters.outbound.persistence.entity.LocationEntity;
import com.empresa.almacen.adapters.outbound.persistence.mapper.ProductMapper;
import com.empresa.almacen.adapters.outbound.persistence.repository.InventoryItemRepository;
import com.empresa.almacen.adapters.outbound.persistence.repository.ProductRepository;
import com.empresa.almacen.application.service.ActivityService;
import com.empresa.almacen.domain.model.Location;
import com.empresa.almacen.domain.model.Product;
import com.empresa.almacen.domain.repository.LocationRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ActivityService activityService;
    private final InventoryItemRepository inventoryRepository; 

    private final LocationRepository locationRepository;

    // Crear un producto
    @Transactional
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@RequestBody Product product) {
        product.setId(null); // asegurar que JPA genere el ID

        // 1. Guardar el producto en la BD
        var productEntity = productRepository.save(productMapper.toEntity(product));

        // 2. Obtener la ubicación por defecto como DOMAIN
        Location defaultLocationDomain = locationRepository.findByCode("PICK-01")
            .orElseThrow(() -> new RuntimeException("Ubicación por defecto no encontrada"));

        // 3. Convertir de dominio -> entidad
        LocationEntity defaultLocationEntity = productMapper.locationToEntity(defaultLocationDomain);

        // 4. Crear inventario inicial
        InventoryItemEntity inventoryItem = new InventoryItemEntity();
        inventoryItem.setProduct(productEntity);
        inventoryItem.setLocation(defaultLocationEntity); // ✅ ahora es LocationEntity
        inventoryItem.setQuantityTotal(0);
        inventoryItem.setQuantityReserved(0);

        inventoryRepository.save(inventoryItem);

        // 5. Log de actividad
        activityService.logActivity(
            "Producto '" + product.getName() + "' creado y agregado al inventario inicial"
        );

        return productMapper.toDomain(productEntity);
    }


    // Listar todos los productos
    @GetMapping
    public List<Product> listProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDomain)
                .toList();
    }

    // Obtener producto por ID
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable UUID id) {
        var entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return productMapper.toDomain(entity);
    }

    // Actualizar un producto
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable UUID id, @RequestBody Product product) {
        var existingEntity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        var updatedEntity = productMapper.toEntity(product);
        updatedEntity.setId(existingEntity.getId()); // mantener el mismo ID
        var savedEntity = productRepository.save(updatedEntity);
        return productMapper.toDomain(savedEntity);
    }
    
    // Eliminar un producto
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable UUID id) {
        var existingEntity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        productRepository.delete(existingEntity);
    }
}

