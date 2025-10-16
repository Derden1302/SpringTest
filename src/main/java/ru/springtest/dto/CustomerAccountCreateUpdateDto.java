package ru.springtest.dto;

import java.util.UUID;

public record CustomerAccountCreateUpdateDto(
        String customerName,
        String accountData
) {

}
