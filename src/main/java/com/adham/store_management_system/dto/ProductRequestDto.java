package com.adham.store_management_system.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.apache.logging.log4j.message.Message;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDto {
    @NotBlank(message = "Product name is required")
    private String name;
    @Positive(message = "Price must be greater than zero")
    private Double price;
    @NotNull(message = "Category id is required")
    private Long categoryId;
    @NotNull(message = "Stock quantity is required")
    @Min( value = 0, message = "Stock quantity cannot be less than zero ")
    private Integer stockQuantity;
}
