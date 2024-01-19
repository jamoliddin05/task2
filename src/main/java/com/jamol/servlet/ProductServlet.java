package com.jamol.servlet;

import com.jamol.model.Product;
import com.jamol.service.ProductService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.*;

@Path("products")
public class ProductServlet {

    private static final ProductService productService = ProductService.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> listAll() {
        return productService.findAll();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Product create(Product product) {
        return productService.save(product);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Product update(@PathParam("id") Integer id, Product product) {
        return productService.update(id, product);
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void delete(@PathParam("id") Integer id) {
        productService.delete(id);
    }
}
