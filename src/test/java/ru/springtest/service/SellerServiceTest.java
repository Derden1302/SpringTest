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
import ru.springtest.domain.Seller;
import ru.springtest.dto.ItemDto;
import ru.springtest.dto.SellerCreateUpdateDto;
import ru.springtest.dto.SellerItemResponseDto;
import ru.springtest.exception.NotFoundException;
import ru.springtest.repository.SellerRepository;
import ru.springtest.service.implementation.SellerServiceImplementation;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
@SpringBootTest
class SellerServiceTest {

    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14");

    @ServiceConnection(name = "redis")
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:8.2.3"))
            .withExposedPorts(6379);

    @Autowired
    SellerServiceImplementation service; // Или интерфейс SellerService

    @Autowired
    SellerRepository sellerRepository;

    @Test
    @Transactional
    void createSeller_success() {
        SellerCreateUpdateDto dto = new SellerCreateUpdateDto(
                "TestSeller",
                List.of(new ItemDto("TestItem"))
        );
        SellerItemResponseDto result = service.createSeller(dto);
        assertThat(result.id()).isNotNull();
        assertThat(result.name()).isEqualTo("TestSeller");
        assertThat(result.item()).hasSize(1);
        assertThat(result.item().get(0).name()).isEqualTo("TestItem");
        Seller savedSeller = sellerRepository.findById(result.id()).orElseThrow();
        assertThat(savedSeller.getName()).isEqualTo("TestSeller");
        assertThat(savedSeller.getItem()).hasSize(1);
        assertThat(savedSeller.getItem().get(0).getName()).isEqualTo("TestItem");
    }

    @Test
    @Transactional
    void updateSeller_success() {
        SellerCreateUpdateDto createDto = new SellerCreateUpdateDto(
                "TestSeller",
                List.of(new ItemDto("TestItem"))
        );
        SellerItemResponseDto created = service.createSeller(createDto);
        UUID id = created.id();
        SellerCreateUpdateDto updateDto = new SellerCreateUpdateDto(
                "UpdatedSeller",
                List.of(new ItemDto("UpdatedItem"))
        );
        SellerItemResponseDto result = service.updateSeller(id, updateDto);
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.name()).isEqualTo("UpdatedSeller");
        Seller updatedSeller = sellerRepository.findById(id).orElseThrow();
        assertThat(updatedSeller.getName()).isEqualTo("UpdatedSeller");
        assertThat(updatedSeller.getItem()).hasSize(1);
        assertThat(updatedSeller.getItem().get(0).getName()).isEqualTo("UpdatedItem");
    }

    @Test
    void updateSeller_notFound() {
        UUID id = UUID.randomUUID();
        SellerCreateUpdateDto dto = new SellerCreateUpdateDto(
                "TestSeller",
                List.of(new ItemDto("TestItem"))
        );
        assertThatThrownBy(() -> service.updateSeller(id, dto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Seller not found with id: " + id);
    }

    @Test
    void getSeller_success() {
        SellerCreateUpdateDto dto = new SellerCreateUpdateDto(
                "TestSeller",
                List.of(new ItemDto("TestItem"))
        );
        SellerItemResponseDto created = service.createSeller(dto);
        UUID id = created.id();
        SellerItemResponseDto result = service.getSeller(id);
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.name()).isEqualTo("TestSeller");
        assertThat(result.item()).hasSize(1);
    }

    @Test
    void getSeller_notFound() {
        UUID id = UUID.randomUUID();
        assertThatThrownBy(() -> service.getSeller(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Seller not found with id: " + id);
    }

    @Test
    @Transactional
    void deleteSeller_success() {
        SellerCreateUpdateDto dto = new SellerCreateUpdateDto(
                "TestSeller",
                List.of(new ItemDto("TestItem"))
        );
        SellerItemResponseDto created = service.createSeller(dto);
        UUID id = created.id();
        service.deleteSeller(id);
        assertThat(sellerRepository.findById(id)).isEmpty();
    }

    @Test
    void deleteSeller_notFound() {
        UUID id = UUID.randomUUID();
        assertThatThrownBy(() -> service.deleteSeller(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Seller not found with id: " + id);
    }
}
