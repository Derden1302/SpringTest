package ru.springtest.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.springtest.domain.Seller;
import ru.springtest.dto.ItemCreateUpdateDto;
import ru.springtest.dto.ItemDto;
import ru.springtest.dto.SellerCreateUpdateDto;
import ru.springtest.dto.SellerItemResponseDto;
import ru.springtest.service.ItemService;
import ru.springtest.service.SellerService;

import java.util.UUID;

@RestController
@RequestMapping(value="sellers-items/v2/api")
@Slf4j
@RequiredArgsConstructor
public class SellerItemsController {
    private final SellerService sellerService;
    private final ItemService itemService;

    @PostMapping("/seller")
    public ResponseEntity<SellerItemResponseDto> createSeller(@RequestBody @Valid SellerCreateUpdateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body((sellerService.createSeller(dto)));
    }

    @PutMapping("/seller/{id}")
    public ResponseEntity<SellerItemResponseDto> updateSellers(@PathVariable UUID id, @RequestBody @Valid SellerCreateUpdateDto dto) {
        return ResponseEntity.ok(sellerService.updateSeller(id, dto));
    }

    @PutMapping("/item/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable UUID id, @RequestBody @Valid ItemCreateUpdateDto dto) {
        return ResponseEntity.ok(itemService.updateItem(id, dto));
    }

    @GetMapping("/seller/{id}")
    public ResponseEntity<SellerItemResponseDto> getSeller(@PathVariable UUID id) {
        return ResponseEntity.ok(sellerService.getSeller(id));
    }

    @DeleteMapping("/seller/{id}")
    public ResponseEntity<Void> deleteBySeller(@PathVariable UUID id) {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }
}
