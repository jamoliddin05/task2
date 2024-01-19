package com.jamol.service;

import com.jamol.dao.ProductDao;
import com.jamol.model.Product;

import java.util.List;

public class ProductService {
    private static final ProductService INSTANCE = new ProductService();
    private static final ProductDao productDao = ProductDao.getInstance();

    private ProductService() {
    }

    public List<Product> findAll() {
        return productDao.findAll();
    }

    public Product save(Product product) {
        return productDao.save(product);
    }

    public Product update(Integer id, Product product) {
        var productFromDb = productDao.findById(id).get();
        productFromDb.setName(product.getName());
        productFromDb.setPrice(product.getPrice());
        productDao.update(productFromDb);
        return productFromDb;
    }

    public void delete(Integer id) {
        productDao.delete(id);
    }

    public static ProductService getInstance() {
        return INSTANCE;
    }
}
