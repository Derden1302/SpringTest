package ru.springtest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.springtest.dto.*;
import ru.springtest.exception.NotFoundException;
import ru.springtest.service.ContractService;
import ru.springtest.service.CustomerAccountService;
import ru.springtest.service.ItemService;
import ru.springtest.service.SellerService;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SellerItemsController.class)
public class SellerItemsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private SellerService sellerService;
    @MockitoBean
    private ItemService itemService;

    @Test
    void createSeller_validRequest_returns201() throws Exception {
        SellerCreateUpdateDto request = new SellerCreateUpdateDto(
                "TestSeller",
                List.of(new ItemDto("TestItem"))
        );
        SellerItemResponseDto response = new SellerItemResponseDto(
                UUID.randomUUID(),
                "TestSeller",
                List.of(new ItemDto("TestItem"))
        );
        when(sellerService.createSeller(request)).thenReturn(response);
        mockMvc.perform(post("/seller/v2/api/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("TestSeller"));
    }

    @Test
    void createSeller_blankName_returns400() throws Exception {
        SellerCreateUpdateDto request = new SellerCreateUpdateDto(
                "   ",
                List.of(new ItemDto("Item1"))
        );

        mockMvc.perform(post("/seller/v2/api/seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createSeller_emptyItems_returns400() throws Exception {
        SellerCreateUpdateDto request = new SellerCreateUpdateDto(
                "Seller",
                List.of()
        );

        mockMvc.perform(post("/seller/v2/api/seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void updateSeller_validRequest_returns201() throws Exception {
        UUID id = UUID.randomUUID();
        SellerCreateUpdateDto request = new SellerCreateUpdateDto(
                "TestSeller",
                List.of(new ItemDto("TestItem"))
        );
        SellerItemResponseDto response = new SellerItemResponseDto(
                id,
                "TestSeller",
                List.of(new ItemDto("TestItem"))
        );
        when(sellerService.updateSeller(id, request)).thenReturn(response);
        mockMvc.perform(put("/seller/v2/api/seller/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TestSeller"));
    }

    @Test
    void updateSeller_invalidUUID_returns400() throws Exception {
        SellerCreateUpdateDto request = new SellerCreateUpdateDto("Seller", List.of(new ItemDto("Item")));

        mockMvc.perform(put("/seller/v2/api/seller/INVALID_UUID")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateSeller_entityNotFound_returns404() throws Exception {
        UUID id = UUID.randomUUID();
        SellerCreateUpdateDto request = new SellerCreateUpdateDto("Seller", List.of(new ItemDto("Item")));

        when(sellerService.updateSeller(eq(id), any()))
                .thenThrow(new NotFoundException("Not found"));

        mockMvc.perform(put("/seller/v2/api/seller/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }


    @Test
    void deleteSeller_validRequest_returns204() throws Exception {
        UUID id = UUID.randomUUID();

        doNothing().when(sellerService).deleteSeller(id);

        mockMvc.perform(delete("/seller/v2/api/seller/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteSeller_notFound_returns404() throws Exception {
        UUID id = UUID.randomUUID();

        doThrow(new NotFoundException("Not found"))
                .when(sellerService).deleteSeller(id);

        mockMvc.perform(delete("/seller/v2/api/seller/" + id))
                .andExpect(status().isNotFound());
    }


    @Test
    void getSeller_validRequest_returns200() throws Exception {
        UUID id = UUID.randomUUID();
        SellerItemResponseDto response = new SellerItemResponseDto(
                id,
                "TestSeller",
                List.of(new ItemDto("TestItem"))
        );
        when(sellerService.getSeller(id)).thenReturn(response);
        mockMvc.perform(get("/seller/v2/api/seller/" + id))
                        .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("TestSeller"));
    }

    @Test
    void getSeller_notFound_returns404() throws Exception {
        UUID id = UUID.randomUUID();

        when(sellerService.getSeller(id))
                .thenThrow(new NotFoundException("Seller not found"));

        mockMvc.perform(get("/seller/v2/api/seller/" + id))
                .andExpect(status().isNotFound());
    }


    @Test
    void updateItem_validRequest_returns204() throws Exception {
        UUID id = UUID.randomUUID();
        ItemCreateUpdateDto request = new ItemCreateUpdateDto(
                id,
                "TestItem"
        );
        ItemDto response = new ItemDto("TestItem");
        when(itemService.updateItem(id, request)).thenReturn(response);
        mockMvc.perform(put("/seller/v2/api/item/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("TestItem"));

    }

    @Test
    void updateItem_nullSellerId_returns400() throws Exception {
        ItemCreateUpdateDto request = new ItemCreateUpdateDto(
                null,
                "Item"
        );

        mockMvc.perform(put("/seller/v2/api/item/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }




}
