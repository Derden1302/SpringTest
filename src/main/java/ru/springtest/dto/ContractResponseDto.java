package ru.springtest.dto;

import java.util.List;
import java.util.Set;

public record ContractResponseDto(
        String name,
        List<HistoryDto> history
) {
}
