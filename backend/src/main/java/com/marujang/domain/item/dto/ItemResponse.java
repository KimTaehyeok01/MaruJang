package com.marujang.domain.item.dto;

import com.marujang.domain.item.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemResponse {

    private Long id;
    private String name;
    private String category;
    private String unit;

    public static ItemResponse from(Item item) {
        return new ItemResponse(item.getId(), item.getName(), item.getCategory(), item.getUnit());
    }
}
