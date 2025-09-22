package com.empresa.almacen.application.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PickingRequestDTO {
    private UUID pickOrderId;
    private List<PickOrderDTO.PickOrderLineDTO> pickedLines;
}
