package com.marujang.domain.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemRequest {

    @NotBlank
    private String name;

    private String category;

    @NotBlank
    private String unit;
}
