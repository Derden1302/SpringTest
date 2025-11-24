package ru.springtest.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.springtest.domain.Item;
import ru.springtest.domain.Seller;
import ru.springtest.dto.ItemDto;
import ru.springtest.dto.SellerCreateUpdateDto;
import ru.springtest.dto.SellerItemResponseDto;
import ru.springtest.exception.NotFoundException;
import ru.springtest.mapper.ItemMapper;
import ru.springtest.mapper.SellerMapper;
import ru.springtest.repository.ItemRepository;
import ru.springtest.repository.SellerRepository;
import ru.springtest.service.ItemService;
import ru.springtest.service.SellerService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellerServiceImplementation implements SellerService {
    private final SellerRepository sellerRepository;
    private final SellerMapper mapper;
    private final ItemMapper itemMapper;

    @CachePut(value = "seller", key = "#result.id()")
    @Transactional
    @Override
    public SellerItemResponseDto createSeller(SellerCreateUpdateDto dto) {
        Seller seller = mapper.toEntity(dto);
        List<Item> item = itemMapper.toEntity(dto.item(), seller);
        mapper.changeSeller(seller, dto, item);
        return mapper.toDto(sellerRepository.save(seller));
    }

    @CachePut(value = "seller", key = "#result.id()")
    @Transactional
    @Override
    public SellerItemResponseDto updateSeller(UUID id, SellerCreateUpdateDto dto) {
        Seller seller = sellerRepository.findById(id).orElseThrow(() -> new NotFoundException("Seller not found with id: " + id));
        List<Item> item = itemMapper.toEntity(dto.item(), seller);
        mapper.changeSeller(seller, dto, item);
        return mapper.toDto(sellerRepository.save(seller));
    }

    @Cacheable(value = "seller", key = "#id")
    @Transactional
    @Override
    public SellerItemResponseDto getSeller(UUID id) {
        Seller seller = sellerRepository.findById(id).orElseThrow(() -> new NotFoundException("Seller not found with id: " + id));
        return mapper.toDto(seller);
    }

    @CacheEvict(value = "seller", key = "#id")
    @Transactional
    @Override
    public void deleteSeller(UUID id) {
        if (!sellerRepository.existsById(id)) {
            throw new NotFoundException("Seller not found with id: " + id);
        }
        sellerRepository.deleteById(id);
    }

}
