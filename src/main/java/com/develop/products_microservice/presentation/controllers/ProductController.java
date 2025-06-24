package com.develop.products_microservice.presentation.controllers;

import com.develop.products_microservice.application.dtos.ProductRequestDTO;
import com.develop.products_microservice.application.dtos.ProductResponseDTO;
import com.develop.products_microservice.application.use_cases.ProductServiceImpl;
import com.develop.products_microservice.domain.interfaces.S3Service;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductServiceImpl productService;

    private final S3Service s3Service;

    public ProductController(ProductServiceImpl productService, S3Service s3Service) {
        this.productService = productService;
        this.s3Service = s3Service;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> listAllProducts() {
        List<ProductResponseDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        ProductResponseDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDTO> register(@RequestPart("product") ProductRequestDTO product,
                                                       @RequestPart("file") MultipartFile file) throws IOException {
        String photoUrl = s3Service.uploadFile(file);
        ProductResponseDTO saved = productService.createProduct(product, photoUrl);
        return ResponseEntity.ok(saved);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id,
                                                            @RequestPart("product") ProductRequestDTO productRequestDTO,
                                                            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        String photoUrl = null;
        if (file != null && !file.isEmpty()) {
            photoUrl = s3Service.uploadFile(file);
        }
        ProductResponseDTO updatedProduct = productService.updateProduct(id, productRequestDTO, photoUrl);
        return ResponseEntity.ok(updatedProduct);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }


}
