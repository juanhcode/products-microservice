package com.develop.products_microservice.controllers;

import com.develop.products_microservice.application.dtos.ProductRequestDTO;
import com.develop.products_microservice.application.dtos.ProductResponseDTO;
import com.develop.products_microservice.application.use_cases.ProductServiceImpl;
import com.develop.products_microservice.domain.interfaces.S3Service;
import com.develop.products_microservice.presentation.controllers.ProductController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductServiceImpl productService;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private ProductController productController;

    private ProductResponseDTO sampleResponse;

    @BeforeEach
    void setup() {
        sampleResponse = new ProductResponseDTO();
        sampleResponse.setCode(1L);
        sampleResponse.setName("Sample Product");
        sampleResponse.setDescription("Test product");
        sampleResponse.setImageUrl("http://url.com/photo.jpg");
        sampleResponse.setUnitPrice(BigDecimal.valueOf(99.99));
        sampleResponse.setQuantity(5);
    }

    @Test
    void testGetAllProducts() {
        when(productService.getAllProducts()).thenReturn(List.of(sampleResponse));

        ResponseEntity<List<ProductResponseDTO>> response = productController.listAllProducts();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("Sample Product", response.getBody().get(0).getName());
        verify(productService).getAllProducts();
    }

    @Test
    void testGetProductById() {
        when(productService.getProductById(1L)).thenReturn(sampleResponse);

        ResponseEntity<ProductResponseDTO> response = productController.getProductById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Sample Product", response.getBody().getName());
        verify(productService).getProductById(1L);
    }

    @Test
    void testCreateProduct() throws IOException {
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setName("New Product");

        MockMultipartFile mockFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "image-content".getBytes());

        when(s3Service.uploadFile(mockFile)).thenReturn("http://url.com/test.jpg");
        when(productService.createProduct(requestDTO, "http://url.com/test.jpg")).thenReturn(sampleResponse);

        ResponseEntity<ProductResponseDTO> response = productController.register(requestDTO, mockFile);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Sample Product", response.getBody().getName());
        verify(s3Service).uploadFile(mockFile);
        verify(productService).createProduct(requestDTO, "http://url.com/test.jpg");
    }

    @Test
    void testUpdateProductWithFile() throws IOException {
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setName("Updated Product");

        MockMultipartFile mockFile = new MockMultipartFile("file", "update.jpg", "image/jpeg", "img".getBytes());

        when(s3Service.uploadFile(mockFile)).thenReturn("http://url.com/update.jpg");
        when(productService.updateProduct(eq(1L), eq(requestDTO), eq("http://url.com/update.jpg"))).thenReturn(sampleResponse);

        ResponseEntity<ProductResponseDTO> response = productController.updateProduct(1L, requestDTO, mockFile);

        assertEquals(200, response.getStatusCodeValue());
        verify(productService).updateProduct(1L, requestDTO, "http://url.com/update.jpg");
    }

    @Test
    void testUpdateProductWithoutFile() throws IOException {
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setName("Updated Product");

        when(productService.updateProduct(1L, requestDTO, null)).thenReturn(sampleResponse);

        ResponseEntity<ProductResponseDTO> response = productController.updateProduct(1L, requestDTO, null);

        assertEquals(200, response.getStatusCodeValue());
        verify(productService).updateProduct(1L, requestDTO, null);
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productService).deleteProduct(1L);

        ResponseEntity<Void> response = productController.deleteProduct(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(productService).deleteProduct(1L);
    }
}
