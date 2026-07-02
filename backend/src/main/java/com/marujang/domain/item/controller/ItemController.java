package com.marujang.domain.item.controller;

import com.marujang.domain.item.dto.ItemRequest;
import com.marujang.domain.item.dto.ItemResponse;
import com.marujang.domain.item.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;

	@PostMapping
	public ResponseEntity<ItemResponse> create(@Valid @RequestBody ItemRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(itemService.create(request));
	}

	@GetMapping
	public List<ItemResponse> findAll() {
		return itemService.findAll();
	}

	@GetMapping("/{id}")
	public ItemResponse findById(@PathVariable Long id) {
		return itemService.findById(id);
	}

	@PutMapping("/{id}")
	public ItemResponse update(@PathVariable Long id, @Valid @RequestBody ItemRequest request) {
		return itemService.update(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		itemService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
