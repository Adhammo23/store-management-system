package com.adham.store_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDto {
    @NotBlank(message = "Product name is required")
    private String name;
    @Positive(message = "Price must be greater than zer")
    private Double price;
    @NotNull(message = "Category id is required")
    private Long categoryId;
}
