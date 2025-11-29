package ru.springtest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.springtest.domain.Seller;
import ru.springtest.dto.ItemDto;
import ru.springtest.dto.SellerCreateUpdateDto;
import ru.springtest.dto.SellerItemResponseDto;
import ru.springtest.exception.NotFoundException;
import ru.springtest.repository.SellerRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SellerServiceTest extends AbstractIntegrationTest{

    @Autowired
    SellerService service;
    @Autowired
    SellerRepository sellerRepository;

    static final UUID SELLER_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    static final String SELLER_NAME = "TestSeller";
    static final String ITEM_NAME = "TestItem";

    @Test
    @Transactional
    void createSeller_success() {
        SellerCreateUpdateDto dto = new SellerCreateUpdateDto(
                SELLER_NAME,
                List.of(new ItemDto(ITEM_NAME))
        );
        SellerItemResponseDto result = service.createSeller(dto);
        assertThat(result.id()).isNotNull();
        assertThat(result.name()).isEqualTo(SELLER_NAME);
        assertThat(result.item()).hasSize(1);
        assertThat(result.item().get(0).name()).isEqualTo(ITEM_NAME);
        Seller savedSeller = sellerRepository.findById(result.id()).orElseThrow();
        assertThat(savedSeller.getName()).isEqualTo(SELLER_NAME);
        assertThat(savedSeller.getItem()).hasSize(1);
        assertThat(savedSeller.getItem().get(0).getName()).isEqualTo(ITEM_NAME);
    }

    @Test
    @Transactional
    void updateSeller_success() {
        SellerCreateUpdateDto createDto = new SellerCreateUpdateDto(
                SELLER_NAME,
                List.of(new ItemDto(ITEM_NAME))
        );
        SellerItemResponseDto created = service.createSeller(createDto);
        SellerCreateUpdateDto updateDto = new SellerCreateUpdateDto(
                "UpdatedSeller",
                List.of(new ItemDto("UpdatedItem"))
        );
        SellerItemResponseDto result = service.updateSeller(created.id(), updateDto);
        assertThat(result.id()).isEqualTo(created.id());
        assertThat(result.name()).isEqualTo("UpdatedSeller");
        Seller updatedSeller = sellerRepository.findById(created.id()).orElseThrow();
        assertThat(updatedSeller.getName()).isEqualTo("UpdatedSeller");
        assertThat(updatedSeller.getItem()).hasSize(1);
        assertThat(updatedSeller.getItem().get(0).getName()).isEqualTo("UpdatedItem");
    }

    @Test
    void updateSeller_notFound() {
        SellerCreateUpdateDto dto = new SellerCreateUpdateDto(
                SELLER_NAME,
                List.of(new ItemDto(ITEM_NAME))
        );
        assertThatThrownBy(() -> service.updateSeller(SELLER_ID, dto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Seller not found with id: " + SELLER_ID);
    }

    @Test
    void getSeller_success() {
        SellerCreateUpdateDto dto = new SellerCreateUpdateDto(
                SELLER_NAME,
                List.of(new ItemDto(ITEM_NAME))
        );
        SellerItemResponseDto created = service.createSeller(dto);
        SellerItemResponseDto result = service.getSeller(created.id());
        assertThat(result.id()).isEqualTo(created.id());
        assertThat(result.name()).isEqualTo(SELLER_NAME);
        assertThat(result.item()).hasSize(1);
    }

    @Test
    void getSeller_notFound() {
        assertThatThrownBy(() -> service.getSeller(SELLER_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Seller not found with id: " + SELLER_ID);
    }

    @Test
    @Transactional
    void deleteSeller_success() {
        SellerCreateUpdateDto dto = new SellerCreateUpdateDto(
                SELLER_NAME,
                List.of(new ItemDto(ITEM_NAME))
        );
        SellerItemResponseDto created = service.createSeller(dto);
        service.deleteSeller(created.id());
        assertThat(sellerRepository.findById(created.id())).isEmpty();
    }

    @Test
    void deleteSeller_notFound() {
        assertThatThrownBy(() -> service.deleteSeller(SELLER_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Seller not found with id: " + SELLER_ID);
    }
}
