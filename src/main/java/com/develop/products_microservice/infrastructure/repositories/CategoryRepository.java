package com.develop.products_microservice.infrastructure.repositories;

import com.develop.products_microservice.domain.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Additional query methods can be defined here if needed

}
