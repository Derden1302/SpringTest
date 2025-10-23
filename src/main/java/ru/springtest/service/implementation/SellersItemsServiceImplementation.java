package ru.springtest.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.springtest.domain.Item;
import ru.springtest.domain.Seller;
import ru.springtest.dto.ItemCreateUpdateDto;
import ru.springtest.dto.ItemDto;
import ru.springtest.dto.SellerCreateUpdateDto;
import ru.springtest.dto.SellerItemResponseDto;
import ru.springtest.mapper.SellerItemMapper;
import ru.springtest.repository.ItemRepository;
import ru.springtest.repository.SellerRepository;
import ru.springtest.service.SellersItemsService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellersItemsServiceImplementation implements SellersItemsService {
    private final SellerRepository sellerRepository;
    private final ItemRepository itemRepository;
    private final SellerItemMapper mapper;

    @Transactional
    @Override
    public Seller createSeller(SellerCreateUpdateDto dto) {
        Seller seller = mapper.toEntity(dto);
        return sellerRepository.save(seller);
    }

    @Transactional
    @Override
    public ItemDto createItem(ItemCreateUpdateDto dto) {
        Seller seller = sellerRepository.findById(dto.sellerId()).orElseThrow(() -> new RuntimeException("Seller not found!"));
        Item item = mapper.toEntity(dto, seller);
        return mapper.toDto(itemRepository.save(item));
    }

    @Transactional
    @Override
    public Seller updateSeller(UUID id, SellerCreateUpdateDto dto) {
        Seller seller = sellerRepository.findById(id).orElseThrow(() -> new RuntimeException("Seller not found!"));
        mapper.changeSeller(seller, dto);
        return sellerRepository.save(seller);
    }

    @Transactional
    @Override
    public ItemDto updateItem(UUID id, ItemCreateUpdateDto dto) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found!"));
        mapper.changeItem(item, dto);
        return mapper.toDto(itemRepository.save(item));
    }

    @Transactional
    @Override
    public SellerItemResponseDto getSeller(UUID sellerId) {
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(() -> new RuntimeException("Seller not found!"));
        List<ItemDto> items = itemRepository.findBySellerId(sellerId).stream().map(mapper::toDto).toList();
        return mapper.toDto(seller, items);
    }

    @Transactional
    @Override
    public void deleteSeller(UUID sellerId) {
        sellerRepository.deleteById(sellerId);
    }

}
