package ru.springtest.dto;

import java.util.List;
import java.util.Set;

public record HistoryResponseDto(
        String name,
        List<ContractDto> contracts
) {
}
