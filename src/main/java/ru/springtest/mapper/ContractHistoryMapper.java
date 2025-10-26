package ru.springtest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.springtest.domain.Contract;
import ru.springtest.domain.History;
import ru.springtest.domain.Item;
import ru.springtest.dto.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ContractHistoryMapper {

    Contract toEntity(ContractCreateUpdateDto dto);

    Contract toEntity(ContractCreateUpdateDto dto, List<History> history);

    History toEntity(HistoryCreateUpdateDto dto);

    History toEntity(HistoryCreateUpdateDto dto, List<Contract> contract);

    ContractResponseDto toDto(Contract contract, List<HistoryDto> histories);

    HistoryResponseDto toDto(History history, List<ContractDto> contracts);

    HistoryDto toDto(History history);

    ContractDto toDto(Contract contract);

    void changeContracts(@MappingTarget Contract contract, ContractCreateUpdateDto dto, List<History> history);

    void changeHistory(@MappingTarget History history, HistoryCreateUpdateDto dto, List<Contract> contract);


}