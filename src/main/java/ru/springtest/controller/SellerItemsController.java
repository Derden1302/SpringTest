package ru.springtest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.springtest.domain.Items;
import ru.springtest.domain.Sellers;
import ru.springtest.dto.ItemCreateDto;
import ru.springtest.dto.ItemsDto;
import ru.springtest.dto.SellerDto;
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
    public ResponseEntity<Sellers> createSeller(@RequestBody SellerDto dto) {
        return ResponseEntity.ok(sellersItemsService.createSeller(dto));
    }

    @PostMapping("/create-items")
    public ResponseEntity<Items> createItems(@RequestBody ItemCreateDto dto) {
        return ResponseEntity.ok(sellersItemsService.createItem(dto));
    }

    @PutMapping("/update-seller")
    public ResponseEntity<Sellers> updateSellers(@RequestBody Sellers seller) {
        return ResponseEntity.ok(sellersItemsService.updateSeller(seller));
    }

    @PutMapping("/update-item")
    public ResponseEntity<Items> updateItem(@RequestBody Items item) {
        return ResponseEntity.ok(sellersItemsService.updateItem(item));
    }

    @GetMapping("/get-by-seller")
    public ResponseEntity<SellersItemsResponseDto> getBySeller(@RequestParam UUID id) {
        return ResponseEntity.ok(sellersItemsService.getSellersItemsBySellerId(id));
    }

    @DeleteMapping("/delete-by-seller")
    public void deleteBySeller(@RequestParam UUID id) {
        sellersItemsService.deleteSeller(id);
    }
}
