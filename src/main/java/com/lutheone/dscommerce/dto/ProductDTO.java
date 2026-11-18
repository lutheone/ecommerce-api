package com.lutheone.dscommerce.dto;

import com.lutheone.dscommerce.entities.Category;
import com.lutheone.dscommerce.entities.Product;
import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

public class ProductDTO {

    private Long id;

    @Size(min = 3, max = 80)
    @NotBlank(message = "Field cannot be blank")
    private String name;

    @Size(min = 10, message = "Description must have at least 10 characters")
    @NotBlank(message = "Field cannot be blank")
    private String description;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private Double price;
    private String imgUrl;

    @NotEmpty(message = "Product must have at least one category")
    private List<CategoryDTO> categories = new ArrayList<>();

    @JsonCreator
    public ProductDTO(Long id, Double price, String imgUrl, String description, String name) {
        this.id = id;
        this.price = price;
        this.imgUrl = imgUrl;
        this.description = description;
        this.name = name;
    }

    public ProductDTO(Product entity) {
        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        price = entity.getPrice();
        imgUrl = entity.getImgUrl();
        for (Category cat : entity.getCategories()) {
            categories.add(new CategoryDTO(cat));
        }
    }

    public Long getId() {
        return id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

}
