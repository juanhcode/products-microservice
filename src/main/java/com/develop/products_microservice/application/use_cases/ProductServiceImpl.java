package com.develop.products_microservice.application.use_cases;
import com.develop.products_microservice.application.dtos.ProductRequestDTO;
import com.develop.products_microservice.application.dtos.ProductResponseDTO;
import com.develop.products_microservice.domain.interfaces.ProductMapper;
import com.develop.products_microservice.domain.interfaces.ProductService;
import com.develop.products_microservice.domain.models.Category;
import com.develop.products_microservice.domain.models.Product;
import com.develop.products_microservice.infrastructure.repositories.CategoryRepository;
import com.develop.products_microservice.infrastructure.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return ProductMapper.toProductResponseDTOList(products);
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return ProductMapper.toProductResponseDTO(product);
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO, String photoUrl) {
        Category category = categoryRepository.findById(productRequestDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + productRequestDTO.getCategoryId()));
        Product product = ProductMapper.toProduct(productRequestDTO, category, photoUrl);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.toProductResponseDTO(savedProduct);
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO, String photoUrl) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        Category category = categoryRepository.findById(productRequestDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + productRequestDTO.getCategoryId()));

        existingProduct.setCategory(category);
        existingProduct.setName(productRequestDTO.getName());
        existingProduct.setDescription(productRequestDTO.getDescription());
        existingProduct.setUnitPrice(productRequestDTO.getUnitPrice());
        existingProduct.setQuantity(productRequestDTO.getQuantity());

        if (photoUrl != null) {
            existingProduct.setImageUrl(photoUrl); // Asegúrate de tener este campo en tu entidad
        }

        Product updatedProduct = productRepository.save(existingProduct);
        return ProductMapper.toProductResponseDTO(updatedProduct);
    }



    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}
