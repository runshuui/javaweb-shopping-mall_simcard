package com.chouwansim.service;

import java.sql.SQLException;
import java.util.List;

import com.chouwansim.dao.ShoppingCartDao;
import com.chouwansim.entity.CartItem;
import com.chouwansim.entity.Product;

public class ShoppingCartService {

    private ShoppingCartDao cartDao =
        new ShoppingCartDao();

    private ProductService productService =
        new ProductService();

    public List<CartItem> findByUserId(
        long userId)
        throws SQLException {

        return cartDao.findByUserId(
            userId
        );
    }

    public void addToCart(
        long userId,
        long productId,
        int quantity)
        throws SQLException {

        if (quantity <= 0) {

            throw new IllegalArgumentException(
                "购买数量必须大于 0。"
            );
        }

        Product product =
            getPurchasableProduct(
                productId
            );

        int currentQuantity =
            cartDao.findQuantity(
                userId,
                productId
            );

        int newQuantity =
            currentQuantity + quantity;

        checkStock(
            product,
            newQuantity
        );

        if (currentQuantity == 0) {

            cartDao.insertItem(
                userId,
                productId,
                newQuantity
            );

        } else {

            cartDao.updateQuantity(
                userId,
                productId,
                newQuantity
            );
        }
    }

    public void updateQuantity(
        long userId,
        long productId,
        int quantity)
        throws SQLException {

        if (quantity <= 0) {

            cartDao.deleteItem(
                userId,
                productId
            );

            return;
        }

        Product product =
            getPurchasableProduct(
                productId
            );

        checkStock(
            product,
            quantity
        );

        cartDao.updateQuantity(
            userId,
            productId,
            quantity
        );
    }

    public void deleteItem(
        long userId,
        long productId)
        throws SQLException {

        cartDao.deleteItem(
            userId,
            productId
        );
    }

    private Product getPurchasableProduct(
        long productId)
        throws SQLException {

        Product product =
            productService.findById(
                productId
            );

        if (product == null) {

            throw new IllegalArgumentException(
                "商品不存在。"
            );
        }

        if (!"PUBLISHED".equals(
                product.getStatus())) {

            throw new IllegalArgumentException(
                "该商品当前未上架。"
            );
        }

        if (product.getPurchasable() != 1) {

            throw new IllegalArgumentException(
                "该商品不支持本站购买。"
            );
        }

        if (product.getPrice() == null) {

            throw new IllegalArgumentException(
                "该商品暂无本站售价。"
            );
        }

        if (product.getStock() == null
            || product.getStock()
               .intValue() <= 0) {

            throw new IllegalArgumentException(
                "该商品当前无库存。"
            );
        }

        return product;
    }

    private void checkStock(
        Product product,
        int quantity) {

        if (quantity >
            product.getStock().intValue()) {

            throw new IllegalArgumentException(
                "购买数量超过当前库存。"
            );
        }
    }
}
