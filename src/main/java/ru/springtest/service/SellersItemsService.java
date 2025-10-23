package ru.springtest.service;


import ru.springtest.domain.Item;
import ru.springtest.domain.Seller;
import ru.springtest.dto.ItemCreateUpdateDto;
import ru.springtest.dto.ItemDto;
import ru.springtest.dto.SellerCreateUpdateDto;
import ru.springtest.dto.SellerItemResponseDto;

import java.util.UUID;

public interface SellersItemsService {
    Seller createSeller(SellerCreateUpdateDto sellers);
    ItemDto createItem(ItemCreateUpdateDto items);
    Seller updateSeller(UUID id, SellerCreateUpdateDto dto);
    ItemDto updateItem(UUID id, ItemCreateUpdateDto dto);
    SellerItemResponseDto getSeller(UUID sellerId);
    void deleteSeller(UUID sellerId);
}
