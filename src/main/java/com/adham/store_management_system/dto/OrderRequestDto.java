package com.adham.store_management_system.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequestDto {
    @NotEmpty(message = "Order must have at least one item")
    @Valid
    private List<OrderItemRequestDto> items;
}
