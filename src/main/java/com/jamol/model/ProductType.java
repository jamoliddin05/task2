package com.jamol.model;


import java.util.Arrays;
import java.util.List;

public enum ProductType {
    PHONE,
    TABLET,
    LAPTOP;

    public static final List<ProductType> CASHED_VALUES = Arrays.asList(values());
}
