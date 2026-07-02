// 주문 상세 품목. Inventory 를 참조해 "어느 공급자의 재고를, 얼마에" 샀는지 특정한다.
// price_snapshot 은 주문 시점 가격을 복사해두는 값 — Inventory.price 가 이후에 바뀌어도 과거 주문 금액은 변하지 않는다.
package com.marujang.domain.order.entity;

import com.marujang.domain.inventory.entity.Inventory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "order_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inventory_id", nullable = false)
	private Inventory inventory;

	@Column(nullable = false)
	private int quantity;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal priceSnapshot;

	@Builder
	public OrderItem(Order order, Inventory inventory, int quantity, BigDecimal priceSnapshot) {
		this.order = order;
		this.inventory = inventory;
		this.quantity = quantity;
		this.priceSnapshot = priceSnapshot;
	}
}
