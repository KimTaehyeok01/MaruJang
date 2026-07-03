package com.marujang.domain.item.service;

import com.marujang.domain.item.dto.ItemRequest;
import com.marujang.domain.item.dto.ItemResponse;
import com.marujang.domain.item.entity.Item;
import com.marujang.domain.item.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

	private final ItemRepository itemRepository;

	@Transactional
	public ItemResponse create(ItemRequest request) {
		Item item = Item.builder()
				.name(request.getName())
				.category(request.getCategory())
				.unit(request.getUnit())
				.build();
		return ItemResponse.from(itemRepository.save(item));
	}

	public List<ItemResponse> findAll() {
		return itemRepository.findAll().stream()
				.map(ItemResponse::from)
				.toList();
	}

	public ItemResponse findById(Long id) {
		return ItemResponse.from(getItem(id));
	}

	@Transactional
	public ItemResponse update(Long id, ItemRequest request) {
		Item item = getItem(id);
		item.update(request.getName(), request.getCategory(), request.getUnit());
		return ItemResponse.from(item);
	}

	@Transactional
	public void delete(Long id) {
		itemRepository.delete(getItem(id));
	}

	private Item getItem(Long id) {
		return itemRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("품목을 찾을 수 없습니다. id=" + id));
	}
}
