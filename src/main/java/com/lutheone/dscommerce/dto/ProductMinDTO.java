package com.lutheone.dscommerce.dto;

import com.lutheone.dscommerce.entities.Product;

public class ProductMinDTO {

    private Long id;
    private String name;
    private Double price;
    private String imgUrl;

    public ProductMinDTO(Long id, Double price, String imgUrl,String name) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public ProductMinDTO(Product entity) {
        id = entity.getId();
        name = entity.getName();
        price = entity.getPrice();
        imgUrl = entity.getImgUrl();
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

    public String getName() {
        return name;
    }

}
