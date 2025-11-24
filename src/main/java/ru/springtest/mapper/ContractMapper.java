package ru.springtest.mapper;

import org.mapstruct.*;
import ru.springtest.domain.Contract;
import ru.springtest.domain.History;
import ru.springtest.dto.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ContractMapper {

    Contract toEntity(ContractCreateUpdateDto dto);

    Contract toEntity(ContractDto entityDto);

    ContractResponseDto toResponseDto(Contract contract);

    List<ContractDto> contract(List<Contract> contract);

    List<Contract> toEntityListContract(List<ContractDto> contractDto);

    ContractDto toDto(Contract contract);

    @Mapping(target = "history", ignore = true)
    default void changeContracts(@MappingTarget Contract contract, ContractCreateUpdateDto dto, List<History> history) {
        contract.setName(dto.name());
        contract.setHistory(history);
    };




}