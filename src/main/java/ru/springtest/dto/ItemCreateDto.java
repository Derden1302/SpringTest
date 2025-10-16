package ru.springtest.dto;

import java.util.UUID;

public record ItemCreateDto(
        UUID sellerId,
        String name
) {
}
