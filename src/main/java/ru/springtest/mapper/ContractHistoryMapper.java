package ru.springtest.mapper;

import org.springframework.stereotype.Component;
import ru.springtest.domain.ContractHistory;
import ru.springtest.dto.ContractHistoryResponseDto;

import java.util.UUID;
@Component
public class ContractHistoryMapper {
    public ContractHistoryResponseDto toDto(ContractHistory contractHistory) {
        return new ContractHistoryResponseDto(
                contractHistory.getContract().getId(),
                contractHistory.getHistory().getId(),
                contractHistory.getContract().getName(),
                contractHistory.getHistory().getName(),
                contractHistory.getEventDate()
        );
    }
}
