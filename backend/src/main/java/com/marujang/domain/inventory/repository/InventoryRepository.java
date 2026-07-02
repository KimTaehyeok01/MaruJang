// Inventory 엔티티에 대한 기본 CRUD 저장소
package com.marujang.domain.inventory.repository;

import com.marujang.domain.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

	Optional<Inventory> findBySupplierIdAndItemId(Long supplierId, Long itemId);
}
