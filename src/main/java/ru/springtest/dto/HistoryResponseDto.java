package ru.springtest.dto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record HistoryResponseDto(
        UUID id,
        String name,
        List<ContractDto> contract
) {
}
