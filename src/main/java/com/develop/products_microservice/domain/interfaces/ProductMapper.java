package com.develop.products_microservice.domain.interfaces;

import com.develop.products_microservice.application.dtos.ProductRequestDTO;
import com.develop.products_microservice.application.dtos.ProductResponseDTO;
import com.develop.products_microservice.domain.models.Category;
import com.develop.products_microservice.domain.models.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

    public static Product toProduct(ProductRequestDTO request, Category category, String photoUrl) {
        Product product = new Product();
        product.setName(request.getName());
        product.setQuantity(request.getQuantity());
        product.setUnitPrice(request.getUnitPrice());
        product.setDescription(request.getDescription());
        product.setImageUrl(photoUrl);
        product.setCategory(category);
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty.");
        }
        return product;
    }

    public static ProductResponseDTO toProductResponseDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setCode(product.getCode());
        dto.setName(product.getName());
        dto.setQuantity(product.getQuantity());
        dto.setUnitPrice(product.getUnitPrice());
        dto.setDescription(product.getDescription());
        dto.setImageUrl(product.getImageUrl());
        dto.setCategoryId(product.getCategory().getId());
        dto.setCategoryName(product.getCategory().getName());
        return dto;
    }

    public static List<ProductResponseDTO> toProductResponseDTOList(List<Product> products) {
        return products.stream()
                .map(ProductMapper::toProductResponseDTO)
                .collect(Collectors.toList());
    }

}
