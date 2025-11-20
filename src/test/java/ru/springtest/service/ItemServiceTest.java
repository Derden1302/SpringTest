package ru.springtest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.springtest.domain.Item;
import ru.springtest.dto.ItemCreateUpdateDto;
import ru.springtest.dto.ItemDto;
import ru.springtest.exception.NotFoundException;
import ru.springtest.mapper.ItemMapper;
import ru.springtest.repository.ItemRepository;
import ru.springtest.service.implementation.ItemServiceImplementation;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemMapper mapper;
    @InjectMocks
    private ItemServiceImplementation service;
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateItem() {
        UUID id = UUID.randomUUID();
        Item item = new Item();
        item.setId(id);
        ItemDto itemDto = new ItemDto("TestItem");
        ItemCreateUpdateDto dto = new ItemCreateUpdateDto(
                id, "TestItem"
        );
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        when(mapper.toDto(item)).thenReturn(itemDto);
        when(itemRepository.save(item)).thenReturn(item);
        ItemDto result = service.updateItem(id, dto);
        assertThat(result.name()).isEqualTo("TestItem");
        verify(mapper).changeItem(item, dto);
    }

    @Test
    void updateItem_notFound() {
        UUID id = UUID.randomUUID();
        Item item = new Item();
        ItemCreateUpdateDto dto = new ItemCreateUpdateDto(
                id, "TestItem"
        );
        when(itemRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.updateItem(id, dto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Item not found");
        verify(itemRepository, never()).save(item);
    }

}
