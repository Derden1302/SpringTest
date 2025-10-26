package ru.springtest.dto;

import java.util.List;
import java.util.UUID;

public record SellerItemResponseDto(
        String name,
        List<ItemDto> items
) {
}
