// Item 엔티티에 대한 기본 CRUD 저장소
package com.marujang.domain.item.repository;

import com.marujang.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
