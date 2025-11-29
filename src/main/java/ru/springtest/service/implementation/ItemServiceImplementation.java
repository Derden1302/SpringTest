package ru.springtest.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.springtest.domain.Item;
import ru.springtest.domain.Seller;
import ru.springtest.dto.ItemCreateUpdateDto;
import ru.springtest.dto.ItemDto;
import ru.springtest.exception.NotFoundException;
import ru.springtest.mapper.ItemMapper;
import ru.springtest.repository.ItemRepository;
import ru.springtest.service.ItemService;
import ru.springtest.service.SellerService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemServiceImplementation implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper mapper;

    @Transactional
    @Override
    public ItemDto updateItem(UUID id, ItemCreateUpdateDto dto) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Item not found with id: " + id));
        mapper.changeItem(item, dto);
        return mapper.toDto(itemRepository.save(item));
    }

}
