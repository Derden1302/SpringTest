package ru.springtest.dto;

import java.util.UUID;

public record ContractDto(
        UUID id,
        String name
) {
}
