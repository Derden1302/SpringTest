package ru.springtest.dto;

import java.util.UUID;

public record HistoryDto(
        UUID id,
        String name
) {
}
