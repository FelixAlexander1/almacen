package com.empresa.almacen.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inbound {
private UUID id;
private String asnNumber;
private LocalDate expectedDate;
private List<InboundItem> items;
private InboundStatus status; 


public enum InboundStatus {
CREATED, PENDING, PARTIAL, RECEIVED
}


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public static class InboundItem {
private UUID productId;
private int expectedQty;
private int receivedQty;
private String lot;
}
}
