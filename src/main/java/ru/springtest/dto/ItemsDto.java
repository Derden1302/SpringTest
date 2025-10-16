package ru.springtest.dto;

import java.util.UUID;

public record ItemsDto(
        UUID id,
        String name
){
}
