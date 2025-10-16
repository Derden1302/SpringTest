package ru.springtest.mapper;

import org.springframework.stereotype.Component;
import ru.springtest.domain.Items;
import ru.springtest.dto.ItemsDto;

@Component
public class ItemsMapper {
    public static ItemsDto toItemsDto (Items items) {
        return new ItemsDto(
                items.getId(),
                items.getName()
        );
    }

}
