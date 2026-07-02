// 품목 조회 응답 바디. 엔티티를 그대로 반환하지 않고 필요한 필드만 노출한다
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
