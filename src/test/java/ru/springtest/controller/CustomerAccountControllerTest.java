package ru.springtest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.springtest.dto.CustomerAccountCreateUpdateDto;
import ru.springtest.dto.CustomerAccountResponseDto;
import ru.springtest.exception.NotFoundException;
import ru.springtest.service.CustomerAccountService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CustomerAccountController.class)
public class CustomerAccountControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    CustomerAccountService service;

    static final UUID CUSTOMER_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    static final String CUSTOMER_NAME = "TestCustomer";
    static final String ACCOUNT_NAME = "TestAccount";

    @Test
    void createWithAccount_validRequest_returns201() throws Exception {
        CustomerAccountCreateUpdateDto request = new CustomerAccountCreateUpdateDto(
                CUSTOMER_NAME, ACCOUNT_NAME
        );
        CustomerAccountResponseDto response = new CustomerAccountResponseDto(
                CUSTOMER_ID,CUSTOMER_NAME, ACCOUNT_NAME
        );
        when(service.createWithAccount(request)).thenReturn(response);
        mockMvc.perform(post("/customer-account/v2/api/сustomer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value(CUSTOMER_ID.toString()))
                .andExpect(jsonPath("$.name").value(CUSTOMER_NAME))
                .andExpect(jsonPath("$.accountData").value(ACCOUNT_NAME));
    }

    @Test
    void createWithAccount_invalidRequest_returns400_withMessage() throws Exception {
        CustomerAccountCreateUpdateDto request = new CustomerAccountCreateUpdateDto(
                "", ACCOUNT_NAME
        );
        mockMvc.perform(post("/customer-account/v2/api/сustomer")
        .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateWithAccount_validRequest_returns201() throws Exception {
        CustomerAccountCreateUpdateDto request = new CustomerAccountCreateUpdateDto(
                "UpdatedCustomer", "UpdatedAccount"
        );
        CustomerAccountResponseDto response = new CustomerAccountResponseDto(
                CUSTOMER_ID,"UpdatedCustomer", "UpdatedAccount"
        );
        when(service.updateWithAccount(any(), eq(CUSTOMER_ID))).thenReturn(response);
        mockMvc.perform(put("/customer-account/v2/api/customer/" + CUSTOMER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(CUSTOMER_ID.toString()))
                .andExpect(jsonPath("$.name").value("UpdatedCustomer"))
                .andExpect(jsonPath("$.accountData").value("UpdatedAccount"));

    }

    @Test
    void updateWithAccount_invalidRequest_returns400_withMessage() throws Exception {
        CustomerAccountCreateUpdateDto request = new CustomerAccountCreateUpdateDto(
                "", "UpdatedAccount"
        );
        mockMvc.perform(put("/customer-account/v2/api/customer/" + CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateWithAccount_notFound_returns404() throws Exception {
        CustomerAccountCreateUpdateDto request = new CustomerAccountCreateUpdateDto(
                "UpdatedCustomer", "UpdatedAccount"
        );
        when(service.updateWithAccount(request, CUSTOMER_ID))
                .thenThrow(new NotFoundException("Customer not found with id: " + CUSTOMER_ID));
        mockMvc.perform(put("/customer-account/v2/api/customer/" + CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Customer not found with id: " + CUSTOMER_ID));
    }

    @Test
    void delete_validRequest_returns204() throws Exception {
        doNothing().when(service).delete(CUSTOMER_ID);
        mockMvc.perform(delete("/customer-account/v2/api/customer/" + CUSTOMER_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_notFound_returns404() throws Exception {
        doThrow(new NotFoundException("Customer not found with id: " + CUSTOMER_ID)).when(service).delete(CUSTOMER_ID);
        mockMvc.perform(delete("/customer-account/v2/api/" + CUSTOMER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void getById_validRequest_returns200() throws Exception {
        CustomerAccountResponseDto response = new CustomerAccountResponseDto(
                CUSTOMER_ID,CUSTOMER_NAME, ACCOUNT_NAME
        );
        when(service.getById(CUSTOMER_ID)).thenReturn(response);
        mockMvc.perform(get("/customer-account/v2/api/customer/" + CUSTOMER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(CUSTOMER_ID.toString()))
                .andExpect(jsonPath("$.name").value(CUSTOMER_NAME))
                .andExpect(jsonPath("$.accountData").value(ACCOUNT_NAME));
    }

    @Test
    void getById_notFound_returns404() throws Exception {
        doThrow(new NotFoundException("Customer not found with id: " + CUSTOMER_ID))
                .when(service).getById(CUSTOMER_ID);
        mockMvc.perform(get("/customer-account/v2/api/customer/" + CUSTOMER_ID))
                .andExpect(status().isNotFound());
    }

}
