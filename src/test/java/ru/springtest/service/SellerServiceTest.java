package ru.springtest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.MockitoAnnotations;
import ru.springtest.domain.Item;
import ru.springtest.domain.Seller;
import ru.springtest.dto.ItemDto;
import ru.springtest.dto.SellerCreateUpdateDto;
import ru.springtest.dto.SellerItemResponseDto;
import ru.springtest.exception.NotFoundException;
import ru.springtest.mapper.ItemMapper;
import ru.springtest.mapper.SellerMapper;
import ru.springtest.repository.SellerRepository;
import ru.springtest.service.implementation.SellerServiceImplementation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SellerServiceTest {
    @Mock
    private SellerRepository sellerRepository;
    @Mock
    private SellerMapper mapper;
    @Mock
    private ItemMapper itemMapper;
    @InjectMocks
    private SellerServiceImplementation service;
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createSeller_success() {
        UUID id = UUID.randomUUID();
        Seller seller = new Seller();
        seller.setId(id);
        Item item = new Item();
        SellerCreateUpdateDto dto = new SellerCreateUpdateDto(
                "TestSeller", List.of(new ItemDto("TestItem")));
        SellerItemResponseDto responseDto = new SellerItemResponseDto(
                id, "TestSeller", List.of(new ItemDto("TestItem"))
        );
        when(mapper.toEntity(dto)).thenReturn(seller);
        when(itemMapper.toEntity(dto.item(),seller)).thenReturn(List.of(item));
        when(sellerRepository.save(seller)).thenReturn(seller);
        when(mapper.toDto(seller)).thenReturn(responseDto);
        SellerItemResponseDto  result = service.createSeller(dto);
        assertThat(result.id()).isEqualTo(id);
        verify(mapper).changeSeller(seller,List.of(item));
    }

    @Test
    void updateSeller_success() {
        UUID id = UUID.randomUUID();
        Seller seller = new Seller();
        seller.setId(id);
        Item item = new Item();
        SellerCreateUpdateDto dto = new SellerCreateUpdateDto(
                "TestSeller", List.of(new ItemDto("TestItem")));
        SellerItemResponseDto responseDto = new SellerItemResponseDto(
                id, "TestSeller", List.of(new ItemDto("TestItem"))
        );
        when(sellerRepository.findById(id)).thenReturn(Optional.of(seller));
        when(itemMapper.toEntity(dto.item(),seller)).thenReturn(List.of(item));
        when(sellerRepository.save(seller)).thenReturn(seller);
        when(mapper.toDto(seller)).thenReturn(responseDto);
        SellerItemResponseDto  result = service.updateSeller(id, dto);
        assertThat(result.id()).isEqualTo(id);
        verify(mapper).changeSeller(seller,List.of(item));
    }

    @Test
    void updateSeller_notFound() {
        UUID id = UUID.randomUUID();
        Seller seller = new Seller();
        SellerCreateUpdateDto dto = new SellerCreateUpdateDto(
                "TestSeller", List.of(new ItemDto("TestItem")));
        when(sellerRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.updateSeller(id, dto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Seller not found with id: " + id);
        verify(sellerRepository, never()).save(seller);
    }

    @Test
    void getSeller_success() {
        UUID id = UUID.randomUUID();
        Seller seller = new Seller();
        seller.setId(id);
        SellerItemResponseDto responseDto = new SellerItemResponseDto(
                id, "TestSeller", List.of(new ItemDto("TestItem"))
        );
        when(sellerRepository.findById(id)).thenReturn(Optional.of(seller));
        when(mapper.toDto(seller)).thenReturn(responseDto);
        SellerItemResponseDto  result = service.getSeller(id);
        assertThat(result.id()).isEqualTo(id);
    }

    @Test
    void getSeller_notFound() {
        UUID id = UUID.randomUUID();
        when(sellerRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getSeller(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Seller not found with id: " + id);
    }

    @Test
    void deleteSeller_success() {
        UUID id = UUID.randomUUID();
        when(sellerRepository.existsById(id)).thenReturn(true);
        service.deleteSeller(id);
        verify(sellerRepository).deleteById(id);
    }

    @Test
    void deleteSeller_notFound() {
        UUID id = UUID.randomUUID();
        when(sellerRepository.existsById(id)).thenReturn(false);
        assertThatThrownBy(() -> service.deleteSeller(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Seller not found with id: " + id);
    }

}
