package ru.springtest.dto;

import java.util.UUID;

public record SellersCreateUpdateDto(
        UUID id,
        String name
) {
}
