package com.chouwansim.service;

import java.sql.SQLException;
import java.util.List;

import com.chouwansim.dao.ProductDao;
import com.chouwansim.entity.Product;

public class ProductService {

    private ProductDao productDao =
        new ProductDao();


    public void addProduct(
        Product product)
        throws SQLException {

        productDao.addProduct(
            product
        );
    }


    public List<Product> findAll()
        throws SQLException {

        return productDao.findAll();
    }


    public Product findById(
        long productId)
        throws SQLException {

        return productDao.findById(
            productId
        );
    }


    public void updateProduct(
        Product product)
        throws SQLException {

        productDao.updateProduct(
            product
        );
    }


    public void updateStatus(
        long productId,
        String status)
        throws SQLException {

        if (!"DRAFT".equals(status)
            && !"PUBLISHED".equals(status)
            && !"ARCHIVED".equals(status)) {

            throw new IllegalArgumentException(
                "商品状态不正确。"
            );
        }


        productDao.updateStatus(
            productId,
            status
        );
    }


    public void deleteProduct(
        long productId)
        throws SQLException {

        productDao.deleteProduct(
            productId
        );
    }


    public List<Product> findPublished(
        String productType,
        String country,
        String form,
        String realname)
        throws SQLException {

        return productDao.findPublished(
            productType,
            country,
            form,
            realname
        );
    }
}