package com.develop.products_microservice.domain.interfaces;

import com.develop.products_microservice.application.dtos.ProductRequestDTO;
import com.develop.products_microservice.application.dtos.ProductResponseDTO;
import com.develop.products_microservice.domain.models.Product;

import java.util.List;

public interface ProductService {
    List<ProductResponseDTO> getAllProducts();
    ProductResponseDTO getProductById(Long id);
    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO, String photoUrl);
    ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO, String photoUrl);
    void deleteProduct(Long id);
}
