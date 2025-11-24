package ru.springtest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.springtest.domain.Item;
import ru.springtest.domain.Seller;
import ru.springtest.dto.ItemCreateUpdateDto;
import ru.springtest.dto.ItemDto;
import ru.springtest.exception.NotFoundException;
import ru.springtest.repository.ItemRepository;
import ru.springtest.repository.SellerRepository;
import ru.springtest.service.implementation.ItemServiceImplementation;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
@SpringBootTest
class ItemServiceTest {

    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14");
    @ServiceConnection(name = "redis")
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:8.2.3"))
            .withExposedPorts(6379);

    @Autowired
    ItemServiceImplementation service; // Или ItemService интерфейс
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    SellerRepository sellerRepository;

    @Test
    @Transactional
    void updateItem_success() {
        Seller seller = new Seller();
        seller.setName("TestSeller");
        seller = sellerRepository.save(seller);
        Item item = new Item();
        item.setName("TestItem");
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
        UUID id = UUID.randomUUID();
        ItemCreateUpdateDto dto = new ItemCreateUpdateDto(
                id, "TestItem"
        );
        assertThatThrownBy(() -> service.updateItem(id, dto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Item not found with id: " + id);
    }
}
