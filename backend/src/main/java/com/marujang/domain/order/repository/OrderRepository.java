// Order 엔티티에 대한 기본 CRUD 저장소
package com.marujang.domain.order.repository;

import com.marujang.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
