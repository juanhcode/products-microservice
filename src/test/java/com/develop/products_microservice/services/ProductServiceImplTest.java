package com.develop.products_microservice.services;

import com.develop.products_microservice.application.dtos.ProductRequestDTO;
import com.develop.products_microservice.application.dtos.ProductResponseDTO;
import com.develop.products_microservice.application.use_cases.ProductServiceImpl;
import com.develop.products_microservice.domain.models.Category;
import com.develop.products_microservice.domain.models.Product;
import com.develop.products_microservice.infrastructure.repositories.CategoryRepository;
import com.develop.products_microservice.infrastructure.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private Category category;
    private ProductRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "Test Category");

        product = new Product();
        product.setCode(1L);
        product.setName("Test Product");
        product.setDescription("Test description");
        product.setUnitPrice(BigDecimal.valueOf(100));
        product.setQuantity(10);
        product.setImageUrl("http://image.jpg");
        product.setCategory(category);

        requestDTO = new ProductRequestDTO();
        requestDTO.setName("New Product");
        requestDTO.setDescription("New desc");
        requestDTO.setQuantity(5);
        requestDTO.setUnitPrice(BigDecimal.valueOf(55));
        requestDTO.setCategoryId(1L);
    }

    @Test
    void getAllProducts_ReturnsList() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductResponseDTO> result = productService.getAllProducts();

        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
        verify(productRepository).findAll();
    }

    @Test
    void getProductById_ExistingId_ReturnsProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponseDTO result = productService.getProductById(1L);

        assertEquals("Test Product", result.getName());
        verify(productRepository).findById(1L);
    }

    @Test
    void getProductById_NotFound_ThrowsException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.getProductById(99L);
        });

        assertEquals("Product not found with id: 99", exception.getMessage());
    }

    @Test
    void createProduct_ValidRequest_SavesProduct() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product saved = invocation.getArgument(0);
            saved.setCode(2L);
            return saved;
        });

        ProductResponseDTO result = productService.createProduct(requestDTO, "http://photo.jpg");

        assertEquals("New Product", result.getName());
        assertEquals(2L, result.getCode());
        verify(categoryRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_ExistingProduct_UpdatesData() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponseDTO result = productService.updateProduct(1L, requestDTO, "http://updated.jpg");

        assertEquals("New Product", result.getName());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_ProductNotFound_ThrowsException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.updateProduct(99L, requestDTO, "url");
        });

        assertTrue(exception.getMessage().contains("Product not found"));
    }

    @Test
    void updateProduct_CategoryNotFound_ThrowsException() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.updateProduct(1L, requestDTO, null);
        });

        assertTrue(exception.getMessage().contains("Category not found"));
    }

    @Test
    void deleteProduct_ExistingId_DeletesSuccessfully() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.deleteProduct(1L);

        verify(productRepository).deleteById(1L);
    }

    @Test
    void deleteProduct_NotFound_ThrowsException() {
        when(productRepository.existsById(99L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.deleteProduct(99L);
        });

        assertEquals("Product not found with id: 99", exception.getMessage());
    }
}
