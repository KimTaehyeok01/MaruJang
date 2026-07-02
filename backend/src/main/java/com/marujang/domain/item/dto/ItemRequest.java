// 품목 생성/수정 요청 바디
package com.marujang.domain.item.dto;

import jakarta.validation.constraints.NotBlank;

public record ItemRequest(
		@NotBlank String name,
		String category,
		@NotBlank String unit
) {
}
