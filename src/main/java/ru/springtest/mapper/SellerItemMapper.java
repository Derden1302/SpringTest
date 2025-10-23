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
public interface SellerItemMapper {

    SellerItemResponseDto toDto(Seller seller, List<ItemDto> items);

    @Mapping(target = "name", source = "dto.name")
    Item toEntity(ItemCreateUpdateDto dto, Seller seller);

    Seller toEntity(SellerCreateUpdateDto dto);

    ItemDto toDto(Item item);

    void changeSeller(@MappingTarget Seller seller, SellerCreateUpdateDto dto);

    void changeItem(@MappingTarget Item item, ItemCreateUpdateDto dto);


}