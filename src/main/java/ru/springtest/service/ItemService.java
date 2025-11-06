package ru.springtest.service;

import org.springframework.transaction.annotation.Transactional;
import ru.springtest.dto.ItemCreateUpdateDto;
import ru.springtest.dto.ItemDto;

import java.util.List;
import java.util.UUID;

public interface ItemService {
    ItemDto updateItem(UUID id, ItemCreateUpdateDto dto);
}
