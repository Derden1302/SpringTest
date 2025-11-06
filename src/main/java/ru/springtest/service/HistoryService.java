package ru.springtest.service;

import org.springframework.transaction.annotation.Transactional;
import ru.springtest.domain.History;
import ru.springtest.dto.HistoryCreateUpdateDto;
import ru.springtest.dto.HistoryDto;
import ru.springtest.dto.HistoryResponseDto;

import java.util.List;
import java.util.UUID;

public interface HistoryService {
    HistoryResponseDto createHistory(HistoryCreateUpdateDto dto);
    HistoryResponseDto updateHistory(UUID id, HistoryCreateUpdateDto dto);
    void deleteHistory(UUID id);
    HistoryResponseDto getHistory(UUID historyId);
}
