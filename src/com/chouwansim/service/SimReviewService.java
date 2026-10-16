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


    /*
     * 当前用户已经发表的评价。
     */
    public List<SimReview> findUserReviews(
        long userId)
        throws SQLException {

        return reviewDao.findByUserId(
            userId
        );
    }


    /*
     * 当前用户还没有评价过的
     * SIM / eSIM 商品。
     */
    public List<Product> findReviewableProducts(
        long userId)
        throws SQLException {

        return reviewDao
            .findReviewableProducts(
                userId
            );
    }


    /*
     * 查询当前用户对某个商品
     * 是否已经发表评价。
     */
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


    /*
     * 打开评价页面时，
     * 查询商品并检查是否允许评价。
     */
    public Product findReviewProduct(
        long productId)
        throws SQLException {

        Product product =
            productService.findById(
                productId
            );

        validateProduct(
            product
        );

        return product;
    }


    /*
     * 保存评价。
     *
     * 第一次评价：
     * INSERT
     *
     * 已经评价过：
     * UPDATE
     */
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

        validateProduct(
            product
        );


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

        content =
            content.trim();


        if (content.length() > 2000) {

            throw new IllegalArgumentException(
                "文字评价不能超过 2000 个字符。"
            );
        }


        review.setContent(
            content
        );


        SimReview existing =
            reviewDao.findByProductAndUser(
                review.getProductId(),
                review.getUserId()
            );


        if (existing == null) {

            reviewDao.insert(
                review
            );

        } else {

            reviewDao.update(
                review
            );
        }
    }


    /*
     * 商品详情页：
     * 查询该商品全部评价。
     */
    public List<SimReview> findProductReviews(
        long productId)
        throws SQLException {

        return reviewDao.findByProductId(
            productId
        );
    }


    /*
     * 商品详情页：
     * 查询评价人数以及六维平均分。
     */
    public ReviewSummary findSummary(
        long productId)
        throws SQLException {

        return reviewDao.findSummary(
            productId
        );
    }


    /*
     * 管理员后台：
     * 查询全部评价。
     */
    public List<SimReview> findAllReviews()
        throws SQLException {

        return reviewDao.findAll();
    }


    /*
     * 管理员后台：
     * 删除评价。
     */
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


    /*
     * 检查该商品是否适用于
     * SIM / eSIM 六维评价。
     */
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


    /*
     * 六项评分必须全部为 1~5。
     */
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