package ru.springtest.dto;

import java.util.UUID;

public record ContractHistoryKeyDto(

        UUID contractId,
        UUID historyId) {
}
