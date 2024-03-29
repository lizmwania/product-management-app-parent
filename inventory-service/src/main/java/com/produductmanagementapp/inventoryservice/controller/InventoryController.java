package com.produductmanagementapp.inventoryservice.controller;

import com.produductmanagementapp.inventoryservice.service.InventoryService;
import com.produductmanagementapp.inventoryservice.dto.InventoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private  final InventoryService inventoryService;
//    @GetMapping("/{sku-code}")
//    @ResponseStatus(HttpStatus.OK)
//    public boolean isInStock(@PathVariable("sku-code") String skuCode){
//        return inventoryService.isInStock(skuCode);
//    }

    @GetMapping
    public List<InventoryResponse> isInStock (@RequestParam List<String>skuCode){
        return inventoryService.isInStock(skuCode);
    }

}
