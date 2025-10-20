package ru.springtest.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.springtest.domain.Items;
import ru.springtest.domain.Sellers;
import ru.springtest.dto.ItemCreateUpdateDto;
import ru.springtest.dto.SellerCreateUpdateDto;
import ru.springtest.dto.SellersItemsResponseDto;
import ru.springtest.mapper.SellersItemsMapper;
import ru.springtest.repository.ItemsRepository;
import ru.springtest.repository.SellersRepository;
import ru.springtest.service.SellersItemsService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellersItemsServiceImplementation implements SellersItemsService {
    private final SellersRepository sellersRepository;
    private final ItemsRepository itemsRepository;
    private final SellersItemsMapper mapper;

    @Transactional
    @Override
    public Sellers createSeller(SellerCreateUpdateDto dto) {
        Sellers seller = mapper.createSeller(dto);
        return sellersRepository.save(seller);
    }

    @Transactional
    @Override
    public Items createItem(ItemCreateUpdateDto dto) {
        Sellers seller = sellersRepository.findById(dto.sellerId()).orElseThrow(() -> new RuntimeException("Seller not found!"));
        Items item = mapper.createItem(dto, seller);
        return itemsRepository.save(item);
    }

    @Transactional
    @Override
    public Sellers updateSeller(UUID id, SellerCreateUpdateDto dto) {
        Sellers seller = sellersRepository.findById(id).orElseThrow(() -> new RuntimeException("Seller not found!"));
        mapper.updateSeller(seller, dto);
        return sellersRepository.save(seller);
    }

    @Transactional
    @Override
    public Items updateItem(UUID id, ItemCreateUpdateDto dto) {
        Items item = itemsRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found!"));
        mapper.updateItem(item, dto);
        return itemsRepository.save(item);
    }

    @Transactional
    @Override
    public SellersItemsResponseDto getSellersItemsBySellerId(UUID sellerId) {
        Sellers sellers = sellersRepository.findById(sellerId).orElseThrow(() -> new RuntimeException("Seller not found!"));
        List<Items> itemsList = itemsRepository.findBySellerId(sellerId);
        return mapper.toDto(sellers, itemsList);
    }

    @Transactional
    @Override
    public void deleteSeller(UUID sellerId) {
        sellersRepository.deleteById(sellerId);
    }

}
