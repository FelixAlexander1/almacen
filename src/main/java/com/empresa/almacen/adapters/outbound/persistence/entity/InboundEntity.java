package com.empresa.almacen.adapters.outbound.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "inbounds")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InboundEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "asn_number", nullable = false, unique = true)
    private String asnNumber;

    @Column(name = "expected_date")
    private LocalDate expectedDate;

    @Enumerated(EnumType.STRING)
    private InboundStatus status;

    // Inicializamos la lista para evitar NullPointerException
    @OneToMany(mappedBy = "inbound", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<InboundItemEntity> items = new ArrayList<>();

    public enum InboundStatus {
        CREATED, PENDING, PARTIAL, RECEIVED
    }

    // Método de conveniencia para agregar items y mantener la relación bidireccional
    public void addItem(InboundItemEntity item) {
        items.add(item);
        item.setInbound(this);
    }
}
