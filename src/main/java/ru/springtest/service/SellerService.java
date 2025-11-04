package ru.springtest.service;


import org.springframework.transaction.annotation.Transactional;
import ru.springtest.domain.Seller;
import ru.springtest.dto.ItemCreateUpdateDto;
import ru.springtest.dto.ItemDto;
import ru.springtest.dto.SellerCreateUpdateDto;
import ru.springtest.dto.SellerItemResponseDto;

import java.util.UUID;

public interface SellerService {
    SellerItemResponseDto createSeller(SellerCreateUpdateDto sellers);
    SellerItemResponseDto getSellerEntity(UUID id);
    SellerItemResponseDto updateSeller(UUID id, SellerCreateUpdateDto dto);
    SellerItemResponseDto getSeller(UUID sellerId);
    void deleteSeller(UUID sellerId);
}
