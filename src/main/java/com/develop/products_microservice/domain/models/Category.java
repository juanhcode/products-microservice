package com.develop.products_microservice.domain.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name = "category")
@Getter
@Setter
public class Category {

    @Id
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;
}

