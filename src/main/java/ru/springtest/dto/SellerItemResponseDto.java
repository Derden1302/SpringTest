package ru.springtest.dto;

import java.util.List;
import java.util.UUID;

public record SellerItemResponseDto(
        UUID id,
        String name,
        List<ItemDto> item
) {
}
