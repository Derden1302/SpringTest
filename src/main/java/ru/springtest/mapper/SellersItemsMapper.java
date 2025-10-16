package ru.springtest.mapper;

import org.springframework.stereotype.Component;
import ru.springtest.domain.Items;
import ru.springtest.domain.Sellers;
import ru.springtest.dto.SellersItemsResponseDto;

import java.util.List;

@Component
public class SellersItemsMapper {
    public SellersItemsResponseDto toDto(Sellers sellers, List<Items> items) {
        return new SellersItemsResponseDto(
                sellers.getId(),
                sellers.getName(),
                items.stream().map(ItemsMapper::toItemsDto).toList()
        );
    }
}
