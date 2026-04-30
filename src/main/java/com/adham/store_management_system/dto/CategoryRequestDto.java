package com.adham.store_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CategoryRequestDto {
    @NotBlank
    private String name;
    private String description;
}
