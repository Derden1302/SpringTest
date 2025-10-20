package ru.springtest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import ru.springtest.domain.Items;
import ru.springtest.domain.Sellers;
import ru.springtest.dto.ItemCreateUpdateDto;
import ru.springtest.dto.ItemsDto;
import ru.springtest.dto.SellerCreateUpdateDto;
import ru.springtest.dto.SellersItemsResponseDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SellersItemsMapper {

    ItemsDto toDto(Items items);

    List<ItemsDto> toDtoList(List<Items> items);

    Sellers createSeller(SellerCreateUpdateDto dto);

    @Mapping(target = "name", source = "dto.name")
    Items createItem(ItemCreateUpdateDto dto, Sellers sellers);

    SellerCreateUpdateDto toDto(Sellers entity);

    void updateSeller(@MappingTarget Sellers sellers, SellerCreateUpdateDto dto);

    void updateItem(@MappingTarget Items items, ItemCreateUpdateDto dto);

    @Mapping(target = "id", source = "sellers.id")
    @Mapping(target = "name", source = "sellers.name")
    SellersItemsResponseDto toDto(Sellers sellers, List<Items> items);
}