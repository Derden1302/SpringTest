package ru.springtest.dto;

import java.util.UUID;

public record ItemDto(
        UUID id,
        String name
){
}
