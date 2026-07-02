package com.marujang.domain.item.dto;

import com.marujang.domain.item.entity.Item;

public record ItemResponse(
		Long id,
		String name,
		String category,
		String unit
) {
	public static ItemResponse from(Item item) {
		return new ItemResponse(item.getId(), item.getName(), item.getCategory(), item.getUnit());
	}
}
