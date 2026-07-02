// OrderItem 엔티티에 대한 기본 CRUD 저장소
package com.marujang.domain.order.repository;

import com.marujang.domain.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	List<OrderItem> findByOrderId(Long orderId);
}
