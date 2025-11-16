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
import ru.springtest.service.CustomerAccountService;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerAccountController.class)
public class CustomerAccountController {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private ContractService contractService;

    @MockitoBean
    private CustomerAccountService customerAccountService;

    @Test
    void createWithAccount_validRequest_returns201() throws Exception {
        CustomerAccountCreateUpdateDto request = new CustomerAccountCreateUpdateDto(
                "TestCustomer",
                "TestAccount"
        );
        CustomerAccountResponseDto response = new CustomerAccountResponseDto(
                UUID.randomUUID(),
                "TestCustomer",
                "TestAccount"
        );
        when(customerAccountService.createWithAccount(request)).thenReturn(response);
        mockMvc.perform(post("/customer-account/v2/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("TestCustomer"))
                .andExpect(jsonPath("$.accountNumber").value("TestAccount"));
    }

    @Test
    void createWithAccount_invalidRequest_returns400() throws Exception {
        CustomerAccountCreateUpdateDto request = new CustomerAccountCreateUpdateDto(
                "",
                ""
        );
        mockMvc.perform(post("/customer-account/v2/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void updateCustomer_validRequest_returns200() throws Exception {
        UUID id = UUID.randomUUID();
        CustomerAccountCreateUpdateDto request = new CustomerAccountCreateUpdateDto(
                "TestCustomer",
                "TestAccount"
        );

        CustomerAccountResponseDto response = new CustomerAccountResponseDto(
                id,
                "TestCustomer",
                "TestAccount"
        );
        when(customerAccountService.updateWithAccount(request, id)).thenReturn(response);
        mockMvc.perform(put("/customer-account/v2/api/customer" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(jsonPath("$.name").value("TestCustomer"))
                .andExpect(jsonPath("$.accountNumber").value("TestAccount"));
    }

    @Test
    void deleteContract_returns204() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(customerAccountService).delete(id);
        mockMvc.perform(delete("/customer-account/v2/api/customer" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void getHistory_returns200() throws Exception {
        UUID id = UUID.randomUUID();
        CustomerAccountResponseDto response = new CustomerAccountResponseDto(
                id,
                "TestCustomer",
                "TestAccount"
        );
        when(customerAccountService.getById(id)).thenReturn(response);
        mockMvc.perform(get("/customer-account/v2/api/customer" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("TestCustomer"))
                .andExpect(jsonPath("$.accountNumber").value("TestAccount"));
    }

}
