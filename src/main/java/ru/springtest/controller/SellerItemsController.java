package ru.springtest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.springtest.domain.Item;
import ru.springtest.domain.Seller;
import ru.springtest.dto.ItemCreateUpdateDto;
import ru.springtest.dto.ItemDto;
import ru.springtest.dto.SellerCreateUpdateDto;
import ru.springtest.dto.SellerItemResponseDto;
import ru.springtest.service.SellersItemsService;

import java.util.UUID;

@RestController
@RequestMapping(value="sellers-items/v1/api")
@Slf4j
@RequiredArgsConstructor
public class SellerItemsController {
    private final SellersItemsService sellersItemsService;

    @PostMapping("/seller")
    public ResponseEntity<Seller> createSeller(@RequestBody SellerCreateUpdateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body((sellersItemsService.createSeller(dto)));
    }

    @PostMapping("/items")
    public ResponseEntity<ItemDto> createItems(@RequestBody ItemCreateUpdateDto dto) {
        ItemDto itemDto = sellersItemsService.createItem(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body((itemDto));
    }

    @PutMapping("/seller/{id}")
    public ResponseEntity<Seller> updateSellers(@PathVariable UUID id, @RequestBody SellerCreateUpdateDto dto) {
        return ResponseEntity.ok(sellersItemsService.updateSeller(id, dto));
    }

    @PutMapping("/item/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable UUID id, @RequestBody ItemCreateUpdateDto dto) {
        return ResponseEntity.ok(sellersItemsService.updateItem(id, dto));
    }

    @GetMapping("/seller/{id}")
    public ResponseEntity<SellerItemResponseDto> getSeller(@PathVariable UUID id) {
        return ResponseEntity.ok(sellersItemsService.getSeller(id));
    }

    @DeleteMapping("/seller/{id}")
    public ResponseEntity<Void> deleteBySeller(@PathVariable UUID id) {
        sellersItemsService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }
}
