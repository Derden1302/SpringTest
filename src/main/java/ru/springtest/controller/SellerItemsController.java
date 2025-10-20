package ru.springtest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.springtest.domain.Items;
import ru.springtest.domain.Sellers;
import ru.springtest.dto.ItemCreateUpdateDto;
import ru.springtest.dto.SellerCreateUpdateDto;
import ru.springtest.dto.SellersItemsResponseDto;
import ru.springtest.service.SellersItemsService;

import java.util.UUID;

@RestController
@RequestMapping(value="sellers-items/v1/api")
@Slf4j
@RequiredArgsConstructor
public class SellerItemsController {
    private final SellersItemsService sellersItemsService;

    @PostMapping("/create-seller")
    public ResponseEntity<Sellers> createSeller(@RequestBody SellerCreateUpdateDto dto) {
        return ResponseEntity.ok(sellersItemsService.createSeller(dto));
    }

    @PostMapping("/create-items")
    public ResponseEntity<Items> createItems(@RequestBody ItemCreateUpdateDto dto) {
        return ResponseEntity.ok(sellersItemsService.createItem(dto));
    }

    @PutMapping("/update-seller/{id}")
    public ResponseEntity<Sellers> updateSellers(@PathVariable UUID id, @RequestBody SellerCreateUpdateDto dto) {
        return ResponseEntity.ok(sellersItemsService.updateSeller(id, dto));
    }

    @PutMapping("/update-item/{id}")
    public ResponseEntity<Items> updateItem(@PathVariable UUID id, @RequestBody ItemCreateUpdateDto dto) {
        return ResponseEntity.ok(sellersItemsService.updateItem(id, dto));
    }

    @GetMapping("/get-by-seller/{id}")
    public ResponseEntity<SellersItemsResponseDto> getBySeller(@PathVariable UUID id) {
        return ResponseEntity.ok(sellersItemsService.getSellersItemsBySellerId(id));
    }

    @DeleteMapping("/delete-by-seller/{id}")
    public void deleteBySeller(@PathVariable UUID id) {
        sellersItemsService.deleteSeller(id);
    }
}
