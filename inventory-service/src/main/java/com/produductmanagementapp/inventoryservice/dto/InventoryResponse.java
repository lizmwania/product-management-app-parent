package com.produductmanagementapp.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryResponse {
    // inventory service dto
    private String skuCode;
    private  boolean isInStock;
}
