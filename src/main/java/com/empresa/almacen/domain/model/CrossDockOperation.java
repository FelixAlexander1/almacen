package com.empresa.almacen.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrossDockOperation {
private UUID id;
private UUID inboundId;
private UUID outShipmentId;
private CrossDockStatus status;


public enum CrossDockStatus {
CREATED, PROCESSING, COMPLETED
}
}
