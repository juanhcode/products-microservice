package com.develop.products_microservice.application.dtos;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductRequestDTO {
    private String name;
    private Integer quantity;
    private BigDecimal unitPrice;
    private String description;
    private Long categoryId;
}
