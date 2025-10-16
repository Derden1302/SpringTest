package ru.springtest.dto;

import java.util.List;
import java.util.UUID;

public record SellersItemsResponseDto (
        UUID id,
        String name,
        List<ItemsDto> list
) {
}
