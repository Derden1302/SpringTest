package ru.springtest.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.springtest.domain.Items;
import ru.springtest.domain.Sellers;
import ru.springtest.dto.ItemCreateDto;
import ru.springtest.dto.ItemsDto;
import ru.springtest.dto.SellerDto;
import ru.springtest.dto.SellersItemsResponseDto;
import ru.springtest.mapper.SellersItemsMapper;
import ru.springtest.repository.ItemsRepository;
import ru.springtest.repository.SellersRepository;
import ru.springtest.service.SellersItemsService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SellersItemsServiceImplementation implements SellersItemsService {
    private final SellersRepository sellersRepository;
    private final ItemsRepository itemsRepository;
    private final SellersItemsMapper mapper;

    @Override
    public Sellers createSeller(SellerDto dto) {
        Sellers seller = new Sellers();
        seller.setName(dto.sellerName());
        return sellersRepository.save(seller);
    }

    @Override
    public Items createItem(ItemCreateDto dto) {
        Items item = new Items();
        item.setName(dto.name());
        item.setSeller(sellersRepository.findById(dto.sellerId()).orElseThrow(() -> new RuntimeException("Seller not found!")));
        return itemsRepository.save(item);
    }

    @Override
    public Sellers updateSeller(Sellers sellers) {
        return sellersRepository.save(sellers);
    }

    @Override
    public Items updateItem(Items items) {
        return itemsRepository.save(items);
    }

    @Override
    public SellersItemsResponseDto getSellersItemsBySellerId(UUID sellerId) {
        Sellers sellers = sellersRepository.findById(sellerId).orElseThrow(() -> new RuntimeException("Seller not found!"));
        List<Items> itemsList = itemsRepository.findBySellerId(sellerId);
        return mapper.toDto(sellers, itemsList);
    }

    @Override
    public void deleteSeller(UUID sellerId) {
        sellersRepository.deleteById(sellerId);
    }

}
