package com.jamol.dao;

import com.jamol.model.Product;
import com.jamol.model.ProductStatus;
import com.jamol.model.ProductType;
import com.jamol.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDao implements Dao<Integer, Product> {

    private static final ProductDao INSTANCE = new ProductDao();

    private static final String FIND_ALL = """
            SELECT *
            FROM product
            """;

    private static final String FIND_BY_ID = FIND_ALL + """
            WHERE id = ?
            """;

    private static final String SAVE = """
            INSERT INTO product (name, price, product_type, product_status)
            VALUES (?, ?, ?, ?)
            """;

    private static final String UPDATE = """
            UPDATE product
            SET name = ?,
                price = ?,
                product_type = ?,
                product_status = ?
            WHERE id = ?
            """;

    private static final String DELETE = """
            DELETE FROM product
            WHERE id = ?
            """;

    private ProductDao() {
    }

    @Override
    public List<Product> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL)) {
            List<Product> products = new ArrayList<>();
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                products.add(buildProduct(resultSet));
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Product> findById(Integer id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setInt(1, id);

            var resultSet = preparedStatement.executeQuery();
            Product product = null;
            if (resultSet.next()) {
                product = buildProduct(resultSet);
            }

            return Optional.ofNullable(product);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product save(Product product) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2, product.getPrice());
            preparedStatement.setInt(3, ProductType.CASHED_VALUES.indexOf(product.getProductType()) + 1);
            preparedStatement.setInt(4, ProductStatus.CASHED_VALUES.indexOf(product.getProductStatus()) + 1);

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                product.setId(generatedKeys.getInt("id"));
            }

            return product;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Product product) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2, product.getPrice());
            preparedStatement.setInt(3, ProductType.CASHED_VALUES.indexOf(product.getProductType()) + 1);
            preparedStatement.setInt(4, ProductStatus.CASHED_VALUES.indexOf(product.getProductStatus()) + 1);
            preparedStatement.setInt(5, product.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Product buildProduct(ResultSet resultSet) throws SQLException {
        return new Product(
                resultSet.getObject("id", Integer.class),
                resultSet.getObject("name", String.class),
                resultSet.getObject("price", Integer.class),
                ProductType.CASHED_VALUES.get(resultSet.getObject("product_type", Integer.class) - 1),
                ProductStatus.CASHED_VALUES.get(resultSet.getObject("product_status", Integer.class) - 1)
        );
    }

    public static ProductDao getInstance() {
        return INSTANCE;
    }
}
