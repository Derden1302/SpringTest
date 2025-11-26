package ru.springtest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.springtest.domain.Item;
import ru.springtest.domain.Seller;
import ru.springtest.dto.ItemCreateUpdateDto;
import ru.springtest.dto.ItemDto;
import ru.springtest.exception.NotFoundException;
import ru.springtest.repository.ItemRepository;
import ru.springtest.repository.SellerRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
@SpringBootTest
class ItemServiceTest extends AbstractIntegrationTest{

    @Autowired
    ItemService service; // Или ItemService интерфейс
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    SellerRepository sellerRepository;

    static final UUID SELLER_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    static final UUID ITEM_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
    static final String SELLER_NAME = "TestSeller";
    static final String ITEM_NAME = "TestItem";

    @Test
    @Transactional
    void updateItem_success() {
        Seller seller = new Seller();
        seller.setName(SELLER_NAME);
        seller = sellerRepository.save(seller);
        Item item = new Item();
        item.setName(ITEM_NAME);
        item = itemRepository.save(item);
        UUID id = item.getId();
        ItemCreateUpdateDto dto = new ItemCreateUpdateDto(
                id, "UpdatedItem"
        );
        ItemDto result = service.updateItem(id, dto);
        assertThat(result.name()).isEqualTo("UpdatedItem");
        Item updatedItem = itemRepository.findById(id).orElseThrow();
        assertThat(updatedItem.getName()).isEqualTo("UpdatedItem");
    }

    @Test
    @Transactional
    void updateItem_notFound() {
        ItemCreateUpdateDto dto = new ItemCreateUpdateDto(
                SELLER_ID, ITEM_NAME
        );
        assertThatThrownBy(() -> service.updateItem(ITEM_ID, dto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Item not found with id: " + ITEM_ID);
    }
}
