package com.empresa.almacen.application.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiveRequestDTO {
    private UUID inboundId;
    private List<InboundDTO.InboundItemDTO> receivedItems;
}
