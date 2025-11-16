package ru.springtest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.springtest.dto.*;
import ru.springtest.service.ContractService;
import ru.springtest.service.HistoryService;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContractHistoryController.class)
class ContractHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private ContractService contractService;

    @MockitoBean
    private HistoryService historyService;

    // ------------------ CREATE CONTRACT ------------------
    @Test
    void createContract_validRequest_returns201() throws Exception {
        ContractCreateUpdateDto request = new ContractCreateUpdateDto(
                "TestContract",
                List.of(new HistoryDto( "History1"))
        );

        ContractResponseDto response = new ContractResponseDto(
                UUID.randomUUID(),
                "TestContract",
                List.of(new HistoryDto( "History1"))
        );

        when(contractService.createContract(request)).thenReturn(response);

        mockMvc.perform(post("/contract-history/v2/api/contract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("TestContract"));
    }

    @Test
    void createContract_invalidRequest_returns400() throws Exception {
        // name пустой → @NotBlank
        ContractCreateUpdateDto request = new ContractCreateUpdateDto(
                "",
                List.of(new HistoryDto( "History"))
        );

        mockMvc.perform(post("/contract-history/v2/api/contract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createContract_emptyHistory_returns400() throws Exception {
        // historyDtos пустой → @NotEmpty
        ContractCreateUpdateDto request = new ContractCreateUpdateDto(
                "Test",
                List.of()
        );

        mockMvc.perform(post("/contract-history/v2/api/contract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    // ------------------ UPDATE CONTRACT ------------------
    @Test
    void updateContract_validRequest_returns200() throws Exception {
        UUID id = UUID.randomUUID();

        ContractCreateUpdateDto request = new ContractCreateUpdateDto(
                "UpdatedName",
                List.of(new HistoryDto( "HistoryUpdated"))
        );

        ContractResponseDto response = new ContractResponseDto(
                id,
                "UpdatedName",
                List.of(new HistoryDto( "HistoryUpdated"))
        );

        when(contractService.updateContract(id, request)).thenReturn(response);
        mockMvc.perform(put("/contract-history/v2/api/contract/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedName"));
    }

    @Test
    void deleteContract_validRequest_returns204() throws Exception {
        UUID id = UUID.randomUUID();

        doNothing().when(contractService).deleteContract(id);

        mockMvc.perform(delete("/contract-history/v2/api/contract/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void getContract_validRequest_returns200() throws Exception {
        UUID id = UUID.randomUUID();

        ContractResponseDto response = new ContractResponseDto(
                id,
                "ContractName",
                List.of(new HistoryDto( "History1"))
        );

        when(contractService.getContract(id)).thenReturn(response);

        mockMvc.perform(get("/contract-history/v2/api/contract/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("ContractName"));
    }

    @Test
    void createHistory_validRequest_returns201() throws Exception {
        HistoryCreateUpdateDto request = new HistoryCreateUpdateDto(
                "HistoryName",
                List.of(new ContractDto( "Contract1"))
        );

        HistoryResponseDto response = new HistoryResponseDto(
                UUID.randomUUID(),
                "HistoryName",
                List.of(new ContractDto( "Contract1"))
        );

        when(historyService.createHistory(request)).thenReturn(response);

        mockMvc.perform(post("/contract-history/v2/api/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("HistoryName"));
    }

    @Test
    void createHistory_invalidRequest_returns400() throws Exception {
        HistoryCreateUpdateDto request = new HistoryCreateUpdateDto(
                "", // name пустой
                List.of(new ContractDto( "C1"))
        );

        mockMvc.perform(post("/contract-history/v2/api/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createHistory_emptyContracts_returns400() throws Exception {
        HistoryCreateUpdateDto request = new HistoryCreateUpdateDto(
                "History",
                List.of() // пустой список
        );

        mockMvc.perform(post("/contract-history/v2/api/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void deleteHistory_returns204() throws Exception {
        UUID id = UUID.randomUUID();

        doNothing().when(historyService).deleteHistory(id);

        mockMvc.perform(delete("/contract-history/v2/api/history/" + id))
                .andExpect(status().isNoContent());
    }


    @Test
    void getHistory_returns200() throws Exception {
        UUID id = UUID.randomUUID();

        HistoryResponseDto response = new HistoryResponseDto(
                id,
                "HistoryTest",
                List.of(new ContractDto( "ContractA"))
        );

        when(historyService.getHistory(id)).thenReturn(response);

        mockMvc.perform(get("/contract-history/v2/api/history/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("HistoryTest"));
    }
}
