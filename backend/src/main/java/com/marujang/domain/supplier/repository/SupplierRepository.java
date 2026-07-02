package com.marujang.domain.supplier.repository;

import com.marujang.domain.supplier.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

	Optional<Supplier> findByUserId(Long userId);
}
