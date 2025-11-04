package ru.springtest.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.springtest.domain.Seller;
import ru.springtest.dto.ItemDto;
import ru.springtest.dto.SellerCreateUpdateDto;
import ru.springtest.dto.SellerItemResponseDto;
import ru.springtest.exception.NotFoundException;
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

    @Transactional
    @Override
    public SellerItemResponseDto createSeller(SellerCreateUpdateDto dto) {
        Seller seller = mapper.toEntity(dto);
        return mapper.toDto(sellerRepository.save(seller));
    }

    @Transactional
    @Override
    public SellerItemResponseDto getSellerEntity(UUID id) {
        Seller seller = sellerRepository.findById(id).orElseThrow(() -> new NotFoundException("Seller not found!"));
        return mapper.toDto(seller);
    }

    @Transactional
    @Override
    public SellerItemResponseDto updateSeller(UUID id, SellerCreateUpdateDto dto) {
        Seller seller = sellerRepository.findById(id).orElseThrow(() -> new NotFoundException("Seller not found!"));
        mapper.changeSeller(seller, dto);
        return mapper.toDto(sellerRepository.save(seller));
    }

    @Transactional
    @Override
    public SellerItemResponseDto getSeller(UUID sellerId) {
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(() -> new NotFoundException("Seller not found!"));
        return mapper.toDto(seller);
    }

    @Transactional
    @Override
    public void deleteSeller(UUID sellerId) {
        sellerRepository.deleteById(sellerId);
    }

}
