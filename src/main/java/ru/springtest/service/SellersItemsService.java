package ru.springtest.service;


import ru.springtest.domain.Items;
import ru.springtest.domain.Sellers;
import ru.springtest.dto.ItemCreateDto;
import ru.springtest.dto.SellerDto;
import ru.springtest.dto.SellersItemsResponseDto;

import java.util.UUID;

public interface SellersItemsService {
    Sellers createSeller(SellerDto sellers);
    Items createItem(ItemCreateDto items);
    Sellers updateSeller(Sellers sellers);
    Items updateItem(Items items);
    SellersItemsResponseDto getSellersItemsBySellerId(UUID sellerId);
    void deleteSeller(UUID sellerId);
}
