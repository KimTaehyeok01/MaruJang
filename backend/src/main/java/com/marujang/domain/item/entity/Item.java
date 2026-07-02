// 식자재 품목 카탈로그 엔티티 (업체 공용)
package com.marujang.domain.item.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	private String category;

	@Column(nullable = false)
	private String unit;

	@Builder
	public Item(String name, String category, String unit) {
		this.name = name;
		this.category = category;
		this.unit = unit;
	}

	public void update(String name, String category, String unit) {
		this.name = name;
		this.category = category;
		this.unit = unit;
	}
}
