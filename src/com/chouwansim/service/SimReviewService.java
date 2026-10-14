package com.chouwansim.service;

import java.sql.SQLException;
import java.util.List;

import com.chouwansim.dao.SimReviewDao;
import com.chouwansim.entity.Product;
import com.chouwansim.entity.ReviewSummary;
import com.chouwansim.entity.SimReview;

public class SimReviewService {

    private SimReviewDao reviewDao =
        new SimReviewDao();

    private ProductService productService =
        new ProductService();

    public List<SimReview> findUserReviews(
        long userId)
        throws SQLException {

        return reviewDao.findByUserId(
            userId
        );
    }

    public List<Product> findReviewableProducts(
        long userId)
        throws SQLException {

        return reviewDao
            .findReviewableProducts(
                userId
            );
    }

    public SimReview findUserReview(
        long productId,
        long userId)
        throws SQLException {

        return reviewDao
            .findByProductAndUser(
                productId,
                userId
            );
    }

    public Product findReviewProduct(
        long productId)
        throws SQLException {

        Product product =
            productService.findById(
                productId
            );

        validateProduct(product);

        return product;
    }

    public void saveReview(
        SimReview review)
        throws SQLException {

        if (review == null) {
            throw new IllegalArgumentException(
                "评价数据不能为空。"
            );
        }

        Product product =
            productService.findById(
                review.getProductId()
            );

        validateProduct(product);

        validateScore(
            review.getApplicationScore(),
            "申请便利度"
        );
        validateScore(
            review.getValueScore(),
            "价格性价比"
        );
        validateScore(
            review.getNetworkScore(),
            "网络体验"
        );
        validateScore(
            review.getRoamingScore(),
            "漫游体验"
        );
        validateScore(
            review.getTransferScore(),
            "设备迁移便利度"
        );
        validateScore(
            review.getRenewalScore(),
            "充值续费便利度"
        );

        String content =
            review.getContent();

        if (content == null) {
            content = "";
        }

        content = content.trim();

        if (content.length() > 2000) {
            throw new IllegalArgumentException(
                "文字评价不能超过 2000 个字符。"
            );
        }

        review.setContent(content);

        SimReview existing =
            reviewDao.findByProductAndUser(
                review.getProductId(),
                review.getUserId()
            );

        if (existing == null) {
            reviewDao.insert(review);
        } else {
            reviewDao.update(review);
        }
    }

    public List<SimReview> findProductReviews(
        long productId)
        throws SQLException {

        return reviewDao.findByProductId(
            productId
        );
    }

    public ReviewSummary findSummary(
        long productId)
        throws SQLException {

        return reviewDao.findSummary(
            productId
        );
    }

    public List<SimReview> findAllReviews()
        throws SQLException {

        return reviewDao.findAll();
    }

    public void deleteReview(
        long reviewId)
        throws SQLException {

        if (reviewId <= 0) {
            throw new IllegalArgumentException(
                "评价编号不正确。"
            );
        }

        reviewDao.deleteById(
            reviewId
        );
    }

    private void validateProduct(
        Product product) {

        if (product == null) {
            throw new IllegalArgumentException(
                "商品不存在。"
            );
        }

        if (!"SIM_ESIM".equals(
                product.getType())) {

            throw new IllegalArgumentException(
                "该商品不使用 SIM/eSIM 六维评价。"
            );
        }

        if (!"PUBLISHED".equals(
                product.getStatus())) {

            throw new IllegalArgumentException(
                "当前商品不可评价。"
            );
        }
    }

    private void validateScore(
        int score,
        String name) {

        if (score < 1
            || score > 5) {

            throw new IllegalArgumentException(
                name
                + "评分必须为 1 到 5 分。"
            );
        }
    }
}
