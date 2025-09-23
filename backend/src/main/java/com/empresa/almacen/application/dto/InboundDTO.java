package com.empresa.almacen.application.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InboundDTO {
    private UUID id;
    private String asnNumber;
    private LocalDate expectedDate;
    private List<InboundItemDTO> items;
    private String status; 


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InboundItemDTO {
        private UUID productId;
        private UUID locationId;    
        private int expectedQty;
        private int receivedQty;
        private String lot;
    }

}
