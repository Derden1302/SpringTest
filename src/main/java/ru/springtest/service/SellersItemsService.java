package ru.springtest.service;


import org.springframework.transaction.annotation.Transactional;
import ru.springtest.domain.Items;
import ru.springtest.domain.Sellers;
import ru.springtest.dto.ItemCreateUpdateDto;
import ru.springtest.dto.SellerCreateUpdateDto;
import ru.springtest.dto.SellersItemsResponseDto;

import java.util.UUID;

public interface SellersItemsService {
    Sellers createSeller(SellerCreateUpdateDto sellers);
    Items createItem(ItemCreateUpdateDto items);
    Sellers updateSeller(UUID id, SellerCreateUpdateDto dto);
    Items updateItem(UUID id, ItemCreateUpdateDto dto);
    SellersItemsResponseDto getSellersItemsBySellerId(UUID sellerId);
    void deleteSeller(UUID sellerId);
}
