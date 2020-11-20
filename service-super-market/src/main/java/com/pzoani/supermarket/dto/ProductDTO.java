package com.pzoani.supermarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private String id;
    private String name;
    private Double price;
    private String categoryId;
    private String vendorId;
}
