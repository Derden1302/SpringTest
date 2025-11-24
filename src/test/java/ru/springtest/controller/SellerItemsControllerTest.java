package ru.springtest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.springtest.dto.ItemCreateUpdateDto;
import ru.springtest.dto.ItemDto;
import ru.springtest.dto.SellerCreateUpdateDto;
import ru.springtest.dto.SellerItemResponseDto;
import ru.springtest.exception.NotFoundException;
import ru.springtest.service.ItemService;
import ru.springtest.service.SellerService;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SellerItemsController.class)
public class SellerItemsControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    SellerService sellerService;
    @MockitoBean
    ItemService itemService;

    static final UUID SELLER_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    static final UUID ITEM_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
    static final String SELLER_NAME = "TestSeller";
    static final String ITEM_NAME = "TestItem";

    @Test
    void createSeller_validRequest_returns201() throws Exception {
        SellerCreateUpdateDto request = new SellerCreateUpdateDto(
                SELLER_NAME, List.of(new ItemDto(ITEM_NAME))
        );
        SellerItemResponseDto response = new SellerItemResponseDto(
                SELLER_ID, SELLER_NAME, List.of(new ItemDto(ITEM_NAME))
        );
        when(sellerService.createSeller(request)).thenReturn(response);
        mockMvc.perform(post("/sellers-items/v2/api/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(SELLER_ID.toString()))
                .andExpect(jsonPath("$.name").value(SELLER_NAME))
                .andExpect(jsonPath("$.item[0].name").value(ITEM_NAME));
    }

    @Test
    void createSeller_invalidRequest_returns400() throws Exception {
        SellerCreateUpdateDto request = new SellerCreateUpdateDto(
                "", List.of(new ItemDto(ITEM_NAME))
        );
        mockMvc.perform(post("/sellers-items/v2/api/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateSeller_validRequest_returns200() throws Exception {
        SellerCreateUpdateDto request = new SellerCreateUpdateDto(
                "UpdatedSeller", List.of(new ItemDto("UpdatedItem"))
        );
        SellerItemResponseDto response = new SellerItemResponseDto(
                SELLER_ID, "UpdatedSeller", List.of(new ItemDto("UpdatedItem"))
        );
        when(sellerService.updateSeller(eq(SELLER_ID), any())).thenReturn(response);
        mockMvc.perform(put("/sellers-items/v2/api/seller/" + SELLER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(SELLER_ID.toString()))
                .andExpect(jsonPath("$.name").value("UpdatedSeller"))
                .andExpect(jsonPath("$.item[0].name").value("UpdatedItem"));
    }

    @Test
    void updateSeller_invalidRequest_returns400() throws Exception {
        SellerCreateUpdateDto request = new SellerCreateUpdateDto(
                "", List.of(new ItemDto(ITEM_NAME))
        );
        mockMvc.perform(put("/sellers-items/v2/api/seller/" + SELLER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateSeller_notFound_returns404() throws Exception {
        SellerCreateUpdateDto request = new SellerCreateUpdateDto(
                "UpdatedSeller", List.of(new ItemDto("UpdatedItem"))
        );
        when(sellerService.updateSeller(eq(SELLER_ID), any()))
                .thenThrow(new NotFoundException("Seller not found with id: " + SELLER_ID));
        mockMvc.perform(put("/sellers-items/v2/api/seller/" +  SELLER_ID)
        .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Seller not found with id: " + SELLER_ID));
    }

    @Test
    void deleteBySeller_validRequest_returns204() throws Exception {
        doNothing().when(sellerService).deleteSeller(SELLER_ID);
        mockMvc.perform(delete("/sellers-items/v2/api/seller/" + SELLER_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBySeller_notFound_returns404() throws Exception {
        doThrow(new NotFoundException("Seller not found with id: " + SELLER_ID))
                .when(sellerService).deleteSeller(SELLER_ID);
        mockMvc.perform(delete("/sellers-items/v2/api/seller/" + SELLER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void getSeller_validRequest_returns200() throws Exception {
        SellerItemResponseDto response = new SellerItemResponseDto(
                SELLER_ID, SELLER_NAME, List.of(new ItemDto(ITEM_NAME))
        );
        when(sellerService.getSeller(SELLER_ID)).thenReturn(response);
        mockMvc.perform(get("/sellers-items/v2/api/seller/" + SELLER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(SELLER_ID.toString()))
                .andExpect(jsonPath("$.name").value(SELLER_NAME))
                .andExpect(jsonPath("$.item[0].name").value(ITEM_NAME));
    }

    @Test
    void getSeller_notFound_returns404() throws Exception {
        doThrow(new NotFoundException("Seller not found with id: " + SELLER_ID))
        .when(sellerService).getSeller(SELLER_ID);
        mockMvc.perform(get("/sellers-items/v2/api/seller/" + SELLER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateItem_validRequest_returns204() throws Exception {
        ItemCreateUpdateDto request = new ItemCreateUpdateDto(SELLER_ID, ITEM_NAME);
        ItemDto response = new ItemDto(ITEM_NAME);
        when(itemService.updateItem(SELLER_ID, request)).thenReturn(response);
        mockMvc.perform(put("/sellers-items/v2/api/item/" + SELLER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(ITEM_NAME));
    }

    @Test
    void updateItem_invalidRequest_returns404_withMessage() throws Exception {
        ItemCreateUpdateDto request = new ItemCreateUpdateDto(SELLER_ID, "");
        mockMvc.perform(put("/sellers-items/v2/api/item/" + ITEM_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateItem_notFound_returns404() throws Exception {
        ItemCreateUpdateDto request = new ItemCreateUpdateDto(SELLER_ID, ITEM_NAME);
        when(itemService.updateItem(eq(ITEM_ID),any()))
                .thenThrow(new NotFoundException("Item not found with id: " + ITEM_ID));
        mockMvc.perform(put("/sellers-items/v2/api/item/" + ITEM_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Item not found with id: " + ITEM_ID));
    }

}

