package com.jamol.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {
    @EqualsAndHashCode.Include()
    private Integer id;
    private String name;
    private Integer price;
    private ProductType productType;
    private ProductStatus productStatus;

    public Product() {
    }

    public Product(Integer id) {
        this.id = id;
    }

    public Product(Integer id, String name, Integer price, ProductType productType, ProductStatus productStatus) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.productType = productType;
        this.productStatus = productStatus;
    }
}

