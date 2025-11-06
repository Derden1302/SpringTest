package ru.springtest.mapper;

import org.mapstruct.*;
import ru.springtest.domain.Item;
import ru.springtest.domain.Seller;
import ru.springtest.dto.ItemCreateUpdateDto;
import ru.springtest.dto.ItemDto;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemMapper {

    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "seller", source = "seller")
    @Mapping(target = "id", ignore = true)
    Item toEntity(ItemCreateUpdateDto dto, Seller seller);

    Item toEntity(ItemDto dto, @Context Seller seller);

    List<Item> toEntity(List<ItemDto> item, @Context Seller seller);

    ItemDto toDto(Item item);

    List<ItemDto> toDto(List<Item> item);

    void changeItem(@MappingTarget Item item, ItemCreateUpdateDto dto);

    @AfterMapping
    default void setSeller(@MappingTarget Item item, @Context Seller seller) {
        item.setSeller(seller);
    }
}
