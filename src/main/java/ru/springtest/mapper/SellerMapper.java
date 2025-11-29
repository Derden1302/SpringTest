package ru.springtest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.springtest.domain.Item;
import ru.springtest.domain.Seller;
import ru.springtest.dto.ItemCreateUpdateDto;
import ru.springtest.dto.ItemDto;
import ru.springtest.dto.SellerCreateUpdateDto;
import ru.springtest.dto.SellerItemResponseDto;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SellerMapper {

    SellerItemResponseDto toDto(Seller seller);

    Seller toEntity(SellerCreateUpdateDto dto);

    Seller toEntity(SellerCreateUpdateDto dto, List<Item> item);

    @Mapping(target = "item", source = "item")
    default void changeSeller(@MappingTarget Seller seller, SellerCreateUpdateDto dto, List<Item> item) {
        seller.setName(dto.name());
        seller.setItem(item);
    }
}