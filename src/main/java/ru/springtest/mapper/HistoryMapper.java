package ru.springtest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.springtest.domain.Contract;
import ru.springtest.domain.History;
import ru.springtest.dto.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HistoryMapper {
    History toEntity(HistoryCreateUpdateDto dto);

    History toEntity(HistoryDto entityDto);

    HistoryResponseDto toResponseDto(History history);

    List<HistoryDto> history(List<History> history);

    List<History> toEntityListHistory(List<HistoryDto>  historyDto);

    HistoryDto toDto(History history);

    void changeHistory(@MappingTarget History history, HistoryCreateUpdateDto dto, List<Contract> contract);

    @Mapping(target = "contract", source = "contract")
    default void changeHistory(@MappingTarget History history, List<Contract> contract) {
        history.setContract(contract);
    }


}
