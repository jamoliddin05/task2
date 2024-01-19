package com.jamol.model;

import java.util.Arrays;
import java.util.List;

public enum ProductStatus {
    AVAILABLE,
    NOT_AVAILABLE;

    public static final List<ProductStatus> CASHED_VALUES = Arrays.asList(values());
}
