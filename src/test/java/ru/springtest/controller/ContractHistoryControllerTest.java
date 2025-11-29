package ru.springtest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.springtest.dto.*;
import ru.springtest.exception.NotFoundException;
import ru.springtest.service.ContractService;
import ru.springtest.service.HistoryService;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContractHistoryController.class)
class ContractHistoryControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    ContractService contractService;
    @MockitoBean
    HistoryService historyService;

    static final UUID CONTRACT_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    static final UUID HISTORY_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
    static final String CONTRACT_NAME = "TestContract";
    static final String HISTORY_NAME = "TestHistory";

    @Test
    void createContract_validRequest_returns201() throws Exception {
        ContractCreateUpdateDto request = new ContractCreateUpdateDto(
                CONTRACT_NAME,
                List.of(new HistoryDto(HISTORY_NAME))
        );
        ContractResponseDto response = new ContractResponseDto(
                CONTRACT_ID,
                CONTRACT_NAME,
                List.of(new HistoryDto(HISTORY_NAME))
        );
        when(contractService.createContract(request)).thenReturn(response);
        mockMvc.perform(post("/contract-history/v2/api/contract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(CONTRACT_ID.toString()))
                .andExpect(jsonPath("$.name").value(CONTRACT_NAME))
                .andExpect(jsonPath("$.history[0].name").value(HISTORY_NAME));
    }

    @Test
    void createContract_invalidRequest_returns400_withMessage() throws Exception {
        ContractCreateUpdateDto request = new ContractCreateUpdateDto(
                "",
                List.of(new HistoryDto(HISTORY_NAME))
        );
        mockMvc.perform(post("/contract-history/v2/api/contract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateContract_validRequest_returns200() throws Exception {
        ContractCreateUpdateDto request = new ContractCreateUpdateDto(
                "UpdatedContract",
                List.of(new HistoryDto("HistoryUpdated"))
        );
        ContractResponseDto response = new ContractResponseDto(
                CONTRACT_ID,
                "UpdatedContract",
                List.of(new HistoryDto("HistoryUpdated"))
        );
        when(contractService.updateContract(eq(CONTRACT_ID), any())).thenReturn(response);
        mockMvc.perform(put("/contract-history/v2/api/contract/" + CONTRACT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CONTRACT_ID.toString()))
                .andExpect(jsonPath("$.name").value("UpdatedContract"))
                .andExpect(jsonPath("$.history[0].name").value("HistoryUpdated"));
    }

    @Test
    void updateContract_invalidRequest_returns400_withMessage() throws Exception {
        ContractCreateUpdateDto request = new ContractCreateUpdateDto(
                "",
                List.of(new HistoryDto("TestHistory"))
        );
        mockMvc.perform(put("/contract-history/v2/api/contract/" + CONTRACT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateContract_notFound_returns404() throws Exception {
        ContractCreateUpdateDto request = new ContractCreateUpdateDto(
                "TestContract",
                List.of(new HistoryDto("TestHistory"))
        );
        when(contractService.updateContract(eq(CONTRACT_ID), any()))
                .thenThrow(new NotFoundException("Contract not found"));
        mockMvc.perform(put("/contract-history/v2/api/contract/" + CONTRACT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotFound()) // Теперь здесь будет 404
                .andExpect(jsonPath("$.message").value("Contract not found"));
    }

    @Test
    void deleteContract_validRequest_returns204() throws Exception {
        doNothing().when(contractService).deleteContract(CONTRACT_ID);
        mockMvc.perform(delete("/contract-history/v2/api/contract/" + CONTRACT_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteContract_notFound_returns404() throws Exception {
        doThrow(new NotFoundException("Contract not found"))
                .when(contractService).deleteContract(CONTRACT_ID);
        mockMvc.perform(delete("/contract-history/v2/api/contract/" + CONTRACT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateHistory_validRequest_returns200() throws Exception {
        HistoryCreateUpdateDto request = new HistoryCreateUpdateDto(
                "UpdatedHistory",
                List.of(new ContractDto("ContractUpdated"))
        );
        HistoryResponseDto response = new HistoryResponseDto(
                HISTORY_ID,
                "UpdatedHistory",
                List.of(new ContractDto("ContractUpdated"))
        );
        when(historyService.updateHistory(eq(HISTORY_ID), any())).thenReturn(response);
        mockMvc.perform(put("/contract-history/v2/api/history/" + HISTORY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedHistory"));
    }

    @Test
    void createHistory_validRequest_returns201() throws Exception {
        HistoryCreateUpdateDto request = new HistoryCreateUpdateDto(
                HISTORY_NAME,
                List.of(new ContractDto(CONTRACT_NAME))
        );
        HistoryResponseDto response = new HistoryResponseDto(
                HISTORY_ID,
                HISTORY_NAME,
                List.of(new ContractDto(CONTRACT_NAME))
        );
        when(historyService.createHistory(request)).thenReturn(response);
        mockMvc.perform(post("/contract-history/v2/api/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(HISTORY_ID.toString()))
                .andExpect(jsonPath("$.name").value(HISTORY_NAME))
                .andExpect(jsonPath("$.contract[0].name").value(CONTRACT_NAME));
    }

    @Test
    void createHistory_invalidRequest_returns400_withMessage() throws Exception {
        HistoryCreateUpdateDto request = new HistoryCreateUpdateDto(
                "",
                List.of(new ContractDto(CONTRACT_NAME))
        );
        mockMvc.perform(post("/contract-history/v2/api/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateHistory_invalidRequest_returns400_withMessage() throws Exception {
        HistoryCreateUpdateDto request = new HistoryCreateUpdateDto(
                "",
                List.of(new ContractDto("TestContract"))
        );
        mockMvc.perform(put("/contract-history/v2/api/history/" + HISTORY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateHistory_notFound_returns404() throws Exception {
        HistoryCreateUpdateDto request = new HistoryCreateUpdateDto(
                "TestHistory",
                List.of(new ContractDto("TestContract"))
        );
        when(historyService.updateHistory(eq(HISTORY_ID), any()))
                .thenThrow(new NotFoundException("History not found"));
        mockMvc.perform(put("/contract-history/v2/api/history/" + HISTORY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotFound()) // Теперь здесь будет 404
                .andExpect(jsonPath("$.message").value("History not found"));
    }

    @Test
    void deleteHistory_validRequest_returns204() throws Exception {
        doNothing().when(historyService).deleteHistory(HISTORY_ID);
        mockMvc.perform(delete("/contract-history/v2/api/history/" + HISTORY_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteHistory_notFound_returns404() throws Exception {
        doThrow(new NotFoundException("History not found"))
                .when(historyService).deleteHistory(HISTORY_ID);
        mockMvc.perform(delete("/contract-history/v2/api/history/" + HISTORY_ID))
                .andExpect(status().isNotFound());
    }

}
