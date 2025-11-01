package ru.springtest.mapper;

import org.mapstruct.*;
import ru.springtest.domain.Contract;
import ru.springtest.domain.History;
import ru.springtest.domain.Item;
import ru.springtest.dto.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ContractHistoryMapper {

    Contract toEntity(ContractCreateUpdateDto dto);

    Contract toEntity(ContractDto entityDto);

    History toEntity(HistoryCreateUpdateDto dto);

    History toEntity(HistoryDto entityDto);

    ContractResponseDto toResponseDto(Contract contract);

    HistoryResponseDto toResponseDto(History history);

    List<ContractDto> contract(List<Contract> contract);

    List<HistoryDto> history(List<History> history);

    HistoryDto toDto(History history);

    ContractDto toDto(Contract contract);

    void changeContracts(@MappingTarget Contract contract, ContractCreateUpdateDto dto, List<History> history);


    @Mapping(target = "history", ignore = true)
    default void changeContracts(@MappingTarget Contract contract, List<History> history) {
        contract.setHistory(history);
    };

    void changeHistory(@MappingTarget History history, HistoryCreateUpdateDto dto, List<Contract> contract);

    @Mapping(target = "contract", source = "contract")
    default void changeHistory(@MappingTarget History history, List<Contract> contract) {
        history.setContract(contract);
    }


}