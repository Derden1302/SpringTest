package ru.springtest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.lang.Contract;
import org.springframework.stereotype.Component;
import ru.springtest.domain.ContractHistory;
import ru.springtest.domain.ContractHistoryId;
import ru.springtest.domain.Contracts;
import ru.springtest.domain.History;
import ru.springtest.dto.ContractCreateUpdateDto;
import ru.springtest.dto.ContractHistoryCreateUpdateDto;
import ru.springtest.dto.ContractHistoryResponseDto;
import ru.springtest.dto.HistoryCreateUpdateDto;

import javax.swing.text.html.parser.Entity;

@Mapper(componentModel = "spring")
public interface ContractHistoryMapper {
    @Mapping(target = "contractId", source = "contractHistory.contract.id")
    @Mapping(target = "historyId", source = "contractHistory.history.id")
    @Mapping(target = "contractName", source = "contractHistory.contract.name")
    @Mapping(target = "historyName", source = "contractHistory.history.name")
    @Mapping(target = "eventDate", source = "contractHistory.eventDate")
    ContractHistoryResponseDto toDto(ContractHistory contractHistory);

    @Mapping(target = "id", source = "id")
    ContractHistory contractHistoryToEntity(ContractHistoryId id, Contracts contract, History history);

    Contracts contractsToEntity(ContractCreateUpdateDto dto);

    History historyToEntity(HistoryCreateUpdateDto dto);

    void contractsUpdateEntity(@MappingTarget Contracts contracts, ContractCreateUpdateDto dto);

    void historyUpdateEntity(@MappingTarget History history, HistoryCreateUpdateDto dto);

    void contractHistoryUpdateEntity(@MappingTarget ContractHistory contractHistory, ContractHistoryCreateUpdateDto dto);
}