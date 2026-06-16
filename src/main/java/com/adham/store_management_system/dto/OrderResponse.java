package com.adham.store_management_system.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderResponse {
    private Long orderId;
    private BigDecimal totalPrice;
    private LocalDateTime dateTime;
    private String createdBy;
    private List<OrderItemResponse> items;
}
