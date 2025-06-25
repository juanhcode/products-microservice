package com.develop.products_microservice.interfaces;

import com.develop.products_microservice.application.dtos.ProductRequestDTO;
import com.develop.products_microservice.application.dtos.ProductResponseDTO;
import com.develop.products_microservice.domain.interfaces.ProductMapper;
import com.develop.products_microservice.domain.models.Category;
import com.develop.products_microservice.domain.models.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    @Test
    void testToProduct_ValidInput_ReturnsProduct() {
        ProductRequestDTO request = new ProductRequestDTO();
        request.setName("Camiseta");
        request.setQuantity(10);
        request.setUnitPrice(BigDecimal.valueOf(29990.50));
        request.setDescription("Camiseta deportiva");
        request.setCategoryId(1L);

        Category category = new Category(1L, "Deportes");
        String photoUrl = "https://bucket.s3.amazonaws.com/img.jpg";

        Product product = ProductMapper.toProduct(request, category, photoUrl);

        assertEquals("Camiseta", product.getName());
        assertEquals(10, product.getQuantity());
        assertEquals(BigDecimal.valueOf(29990.50), product.getUnitPrice());
        assertEquals("Camiseta deportiva", product.getDescription());
        assertEquals(photoUrl, product.getImageUrl());
        assertEquals(category, product.getCategory());
    }

    @Test
    void testToProduct_InvalidName_ThrowsException() {
        ProductRequestDTO request = new ProductRequestDTO();
        request.setName(""); // nombre inválido

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                ProductMapper.toProduct(request, new Category(), "url.jpg")
        );

        assertEquals("Product name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void testToProductResponseDTO_ValidProduct_ReturnsDTO() {
        Category category = new Category(5L, "Tecnología");

        Product product = new Product();
        product.setCode(100L);
        product.setName("Laptop");
        product.setQuantity(3);
        product.setUnitPrice(BigDecimal.valueOf(2500000));
        product.setDescription("Laptop de alto rendimiento");
        product.setImageUrl("http://img.com/laptop.jpg");
        product.setCategory(category);

        ProductResponseDTO dto = ProductMapper.toProductResponseDTO(product);

        assertEquals(100L, dto.getCode());
        assertEquals("Laptop", dto.getName());
        assertEquals(3, dto.getQuantity());
        assertEquals(BigDecimal.valueOf(2500000), dto.getUnitPrice());
        assertEquals("Laptop de alto rendimiento", dto.getDescription());
        assertEquals("http://img.com/laptop.jpg", dto.getImageUrl());
        assertEquals(5L, dto.getCategoryId());
        assertEquals("Tecnología", dto.getCategoryName());
    }

    @Test
    void testToProductResponseDTOList_MultipleProducts() {
        Category cat = new Category(1L, "Hogar");

        Product p1 = new Product();
        p1.setCode(1L);
        p1.setName("Silla");
        p1.setQuantity(5);
        p1.setUnitPrice(BigDecimal.valueOf(100000));
        p1.setDescription("Silla de madera");
        p1.setImageUrl("http://img.com/silla.jpg");
        p1.setCategory(cat);

        Product p2 = new Product();
        p2.setCode(2L);
        p2.setName("Mesa");
        p2.setQuantity(2);
        p2.setUnitPrice(BigDecimal.valueOf(300000));
        p2.setDescription("Mesa de comedor");
        p2.setImageUrl("http://img.com/mesa.jpg");
        p2.setCategory(cat);

        List<ProductResponseDTO> result = ProductMapper.toProductResponseDTOList(List.of(p1, p2));

        assertEquals(2, result.size());
        assertEquals("Silla", result.get(0).getName());
        assertEquals("Mesa", result.get(1).getName());
        assertEquals(1L, result.get(0).getCategoryId());
    }
}
