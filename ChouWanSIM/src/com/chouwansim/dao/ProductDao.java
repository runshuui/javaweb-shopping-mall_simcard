package com.chouwansim.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.List;

import com.chouwansim.entity.Product;
import com.chouwansim.entity.SimProduct;
import com.chouwansim.util.DBUtil;

public class ProductDao {

    private static final long SIM_PREFIX =
        61010220050406L;

    private static final long EUICC_PREFIX =
        61082820050501L;

    public void addProduct(Product product)
        throws SQLException {

        Connection conn = null;

        try {

            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            long productId =
                generateProductId(
                    conn,
                    product.getType()
                );

            product.setId(productId);

            insertProduct(
                conn,
                product
            );

            if ("SIM_ESIM".equals(
                    product.getType())) {

                SimProduct sim =
                    product.getSimProduct();

                if (sim != null) {

                    sim.setProductId(
                        productId
                    );

                    insertSimProduct(
                        conn,
                        sim
                    );
                }
            }

            conn.commit();

        } catch (SQLException e) {

            if (conn != null) {

                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            throw e;

        } finally {

            if (conn != null) {

                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private long generateProductId(
        Connection conn,
        String productType)
        throws SQLException {

        long prefix;

        if ("EUICC".equals(productType)) {
            prefix = EUICC_PREFIX;
        } else {
            prefix = SIM_PREFIX;
        }

        long minimum =
            prefix * 10000L;

        long maximum =
            minimum + 9999L;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            String sql =
                "SELECT MAX(pduct_id) "
                + "FROM product "
                + "WHERE pduct_id >= ? "
                + "AND pduct_id <= ?";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setLong(1, minimum);
            pstmt.setLong(2, maximum);

            rs = pstmt.executeQuery();

            if (rs.next()) {

                long maxId =
                    rs.getLong(1);

                if (!rs.wasNull()
                    && maxId >= minimum) {

                    if (maxId >= maximum) {

                        throw new SQLException(
                            "商品编号已经达到上限。"
                        );
                    }

                    return maxId + 1L;
                }
            }

            return minimum + 1L;

        } finally {

            if (rs != null) {

                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (pstmt != null) {

                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void insertProduct(
        Connection conn,
        Product product)
        throws SQLException {

        PreparedStatement pstmt = null;

        try {

            String sql =
                "INSERT INTO product ("
                + "pduct_id, "
                + "pduct_name, "
                + "pduct_type, "
                + "pduct_short_description, "
                + "pduct_description, "
                + "pduct_cover_image, "
                + "pduct_purchasable, "
                + "pduct_price, "
                + "pduct_stock, "
                + "pduct_status, "
                + "pduct_featured, "
                + "pduct_display_order, "
                + "pduct_created_at, "
                + "pduct_updated_at"
                + ") VALUES ("
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "NOW(), NOW()"
                + ")";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setLong(
                1,
                product.getId()
            );

            pstmt.setString(
                2,
                product.getName()
            );

            pstmt.setString(
                3,
                product.getType()
            );

            pstmt.setString(
                4,
                product.getShortDescription()
            );

            pstmt.setString(
                5,
                product.getDescription()
            );

            pstmt.setString(
                6,
                product.getCoverImage()
            );

            pstmt.setInt(
                7,
                product.getPurchasable()
            );

            if (product.getPrice() != null) {

                pstmt.setBigDecimal(
                    8,
                    product.getPrice()
                );

            } else {

                pstmt.setNull(
                    8,
                    Types.DECIMAL
                );
            }

            if (product.getStock() != null) {

                pstmt.setInt(
                    9,
                    product.getStock().intValue()
                );

            } else {

                pstmt.setNull(
                    9,
                    Types.INTEGER
                );
            }

            pstmt.setString(
                10,
                product.getStatus()
            );

            pstmt.setInt(
                11,
                product.getFeatured()
            );

            pstmt.setInt(
                12,
                product.getDisplayOrder()
            );

            pstmt.executeUpdate();

        } finally {

            if (pstmt != null) {

                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void insertSimProduct(
        Connection conn,
        SimProduct sim)
        throws SQLException {

        PreparedStatement pstmt = null;

        try {

            String sql =
                "INSERT INTO sim_product ("
                + "simprod_pduct_id, "
                + "simprod_form, "
                + "simprod_operator, "
                + "simprod_country_region, "
                + "simprod_realname_required, "
                + "simprod_official_url, "
                + "simprod_application_guide, "
                + "simprod_keepalive_guide"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setLong(
                1,
                sim.getProductId()
            );

            pstmt.setString(
                2,
                sim.getForm()
            );

            pstmt.setString(
                3,
                sim.getOperator()
            );

            pstmt.setString(
                4,
                sim.getCountryRegion()
            );

            pstmt.setInt(
                5,
                sim.getRealnameRequired()
            );

            pstmt.setString(
                6,
                sim.getOfficialUrl()
            );

            pstmt.setString(
                7,
                sim.getApplicationGuide()
            );

            pstmt.setString(
                8,
                sim.getKeepaliveGuide()
            );

            pstmt.executeUpdate();

        } finally {

            if (pstmt != null) {

                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Product> findAll()
        throws SQLException {

        List<Product> products =
            new ArrayList<Product>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                baseSelectSql()
                + " ORDER BY "
                + "p.pduct_display_order ASC, "
                + "p.pduct_id ASC";

            pstmt =
                conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next()) {

                products.add(
                    mapProduct(rs)
                );
            }

            return products;

        } finally {

            DBUtil.close(
                rs,
                pstmt,
                conn
            );
        }
    }

    public Product findById(long productId)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                baseSelectSql()
                + " WHERE p.pduct_id = ?";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setLong(
                1,
                productId
            );

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapProduct(rs);
            }

            return null;

        } finally {

            DBUtil.close(
                rs,
                pstmt,
                conn
            );
        }
    }

    public void updateProduct(
        Product product)
        throws SQLException {

        Connection conn = null;

        try {

            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            updateProductMain(
                conn,
                product
            );

            if ("SIM_ESIM".equals(
                    product.getType())) {

                SimProduct sim =
                    product.getSimProduct();

                if (sim == null) {

                    throw new SQLException(
                        "SIM/eSIM 商品缺少 SIM 产品资料。"
                    );
                }

                sim.setProductId(
                    product.getId()
                );

                updateOrInsertSimProduct(
                    conn,
                    sim
                );

            } else if ("EUICC".equals(
                    product.getType())) {

                deleteSimProduct(
                    conn,
                    product.getId()
                );
            }

            conn.commit();

        } catch (SQLException e) {

            if (conn != null) {

                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            throw e;

        } finally {

            if (conn != null) {

                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateProductMain(
        Connection conn,
        Product product)
        throws SQLException {

        PreparedStatement pstmt = null;

        try {

            String sql =
                "UPDATE product SET "
                + "pduct_name = ?, "
                + "pduct_type = ?, "
                + "pduct_short_description = ?, "
                + "pduct_description = ?, "
                + "pduct_cover_image = ?, "
                + "pduct_purchasable = ?, "
                + "pduct_price = ?, "
                + "pduct_stock = ?, "
                + "pduct_status = ?, "
                + "pduct_featured = ?, "
                + "pduct_display_order = ?, "
                + "pduct_updated_at = NOW() "
                + "WHERE pduct_id = ?";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setString(
                1,
                product.getName()
            );

            pstmt.setString(
                2,
                product.getType()
            );

            pstmt.setString(
                3,
                product.getShortDescription()
            );

            pstmt.setString(
                4,
                product.getDescription()
            );

            pstmt.setString(
                5,
                product.getCoverImage()
            );

            pstmt.setInt(
                6,
                product.getPurchasable()
            );

            if (product.getPrice() != null) {

                pstmt.setBigDecimal(
                    7,
                    product.getPrice()
                );

            } else {

                pstmt.setNull(
                    7,
                    Types.DECIMAL
                );
            }

            if (product.getStock() != null) {

                pstmt.setInt(
                    8,
                    product.getStock().intValue()
                );

            } else {

                pstmt.setNull(
                    8,
                    Types.INTEGER
                );
            }

            pstmt.setString(
                9,
                product.getStatus()
            );

            pstmt.setInt(
                10,
                product.getFeatured()
            );

            pstmt.setInt(
                11,
                product.getDisplayOrder()
            );

            pstmt.setLong(
                12,
                product.getId()
            );

            pstmt.executeUpdate();

        } finally {

            if (pstmt != null) {

                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateOrInsertSimProduct(
        Connection conn,
        SimProduct sim)
        throws SQLException {

        PreparedStatement pstmt = null;

        try {

            String sql =
                "UPDATE sim_product SET "
                + "simprod_form = ?, "
                + "simprod_operator = ?, "
                + "simprod_country_region = ?, "
                + "simprod_realname_required = ? "
                + "WHERE simprod_pduct_id = ?";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setString(
                1,
                sim.getForm()
            );

            pstmt.setString(
                2,
                sim.getOperator()
            );

            pstmt.setString(
                3,
                sim.getCountryRegion()
            );

            pstmt.setInt(
                4,
                sim.getRealnameRequired()
            );

            pstmt.setLong(
                5,
                sim.getProductId()
            );

            int affected =
                pstmt.executeUpdate();

            pstmt.close();
            pstmt = null;

            if (affected == 0) {

                insertSimProduct(
                    conn,
                    sim
                );
            }

        } finally {

            if (pstmt != null) {

                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateStatus(
        long productId,
        String status)
        throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {

            conn = DBUtil.getConnection();

            String sql =
                "UPDATE product "
                + "SET pduct_status = ?, "
                + "pduct_updated_at = NOW() "
                + "WHERE pduct_id = ?";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setString(
                1,
                status
            );

            pstmt.setLong(
                2,
                productId
            );

            pstmt.executeUpdate();

        } finally {

            DBUtil.close(
                null,
                pstmt,
                conn
            );
        }
    }

    public void deleteProduct(long productId)
        throws SQLException {

        Connection conn = null;
        PreparedStatement productStmt = null;

        try {

            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            deleteSimProduct(
                conn,
                productId
            );

            String sql =
                "DELETE FROM product "
                + "WHERE pduct_id = ?";

            productStmt =
                conn.prepareStatement(sql);

            productStmt.setLong(
                1,
                productId
            );

            productStmt.executeUpdate();

            conn.commit();

        } catch (SQLException e) {

            if (conn != null) {

                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            throw e;

        } finally {

            if (productStmt != null) {

                try {
                    productStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (conn != null) {

                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void deleteSimProduct(
        Connection conn,
        long productId)
        throws SQLException {

        PreparedStatement pstmt = null;

        try {

            String sql =
                "DELETE FROM sim_product "
                + "WHERE simprod_pduct_id = ?";

            pstmt =
                conn.prepareStatement(sql);

            pstmt.setLong(
                1,
                productId
            );

            pstmt.executeUpdate();

        } finally {

            if (pstmt != null) {

                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Product> findPublished(
        String productType,
        String country,
        String form,
        String realname)
        throws SQLException {

        List<Product> products =
            new ArrayList<Product>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = DBUtil.getConnection();

            StringBuffer sql =
                new StringBuffer();

            sql.append(
                baseSelectSql()
            );

            sql.append(
                " WHERE p.pduct_status = 'PUBLISHED' "
            );

            sql.append(
                "AND p.pduct_type = ? "
            );

            List<Object> params =
                new ArrayList<Object>();

            params.add(
                productType
            );

            if ("SIM_ESIM".equals(
                    productType)) {

                if (country != null
                    && country.length() > 0) {

                    sql.append(
                        "AND s.simprod_country_region = ? "
                    );

                    params.add(
                        country
                    );
                }

                if (form != null
                    && form.length() > 0) {

                    sql.append(
                        "AND ("
                        + "s.simprod_form = ? "
                        + "OR s.simprod_form = 'BOTH'"
                        + ") "
                    );

                    params.add(
                        form
                    );
                }

                if ("0".equals(realname)
                    || "1".equals(realname)) {

                    sql.append(
                        "AND s.simprod_realname_required = ? "
                    );

                    params.add(
                        new Integer(
                            realname
                        )
                    );
                }
            }

            sql.append(
                "ORDER BY "
                + "p.pduct_display_order ASC, "
                + "p.pduct_id ASC"
            );

            pstmt =
                conn.prepareStatement(
                    sql.toString()
                );

            for (int i = 0;
                 i < params.size();
                 i++) {

                Object value =
                    params.get(i);

                if (value instanceof Integer) {

                    pstmt.setInt(
                        i + 1,
                        ((Integer) value)
                            .intValue()
                    );

                } else {

                    pstmt.setString(
                        i + 1,
                        (String) value
                    );
                }
            }

            rs =
                pstmt.executeQuery();

            while (rs.next()) {

                products.add(
                    mapProduct(rs)
                );
            }

            return products;

        } finally {

            DBUtil.close(
                rs,
                pstmt,
                conn
            );
        }
    }

    private String baseSelectSql() {

        return
            "SELECT "
            + "p.pduct_id, "
            + "p.pduct_name, "
            + "p.pduct_type, "
            + "p.pduct_short_description, "
            + "p.pduct_description, "
            + "p.pduct_cover_image, "
            + "p.pduct_purchasable, "
            + "p.pduct_price, "
            + "p.pduct_stock, "
            + "p.pduct_status, "
            + "p.pduct_featured, "
            + "p.pduct_display_order, "
            + "s.simprod_form, "
            + "s.simprod_operator, "
            + "s.simprod_country_region, "
            + "s.simprod_realname_required, "
            + "s.simprod_official_url, "
            + "s.simprod_application_guide, "
            + "s.simprod_keepalive_guide "
            + "FROM product p "
            + "LEFT JOIN sim_product s "
            + "ON p.pduct_id = s.simprod_pduct_id ";
    }

    private Product mapProduct(
        ResultSet rs)
        throws SQLException {

        Product product =
            new Product();

        product.setId(
            rs.getLong(
                "pduct_id"
            )
        );

        product.setName(
            rs.getString(
                "pduct_name"
            )
        );

        product.setType(
            rs.getString(
                "pduct_type"
            )
        );

        product.setShortDescription(
            rs.getString(
                "pduct_short_description"
            )
        );

        product.setDescription(
            rs.getString(
                "pduct_description"
            )
        );

        product.setCoverImage(
            rs.getString(
                "pduct_cover_image"
            )
        );

        product.setPurchasable(
            rs.getInt(
                "pduct_purchasable"
            )
        );

        product.setPrice(
            rs.getBigDecimal(
                "pduct_price"
            )
        );

        int stock =
            rs.getInt(
                "pduct_stock"
            );

        if (rs.wasNull()) {

            product.setStock(null);

        } else {

            product.setStock(
                new Integer(stock)
            );
        }

        product.setStatus(
            rs.getString(
                "pduct_status"
            )
        );

        product.setFeatured(
            rs.getInt(
                "pduct_featured"
            )
        );

        product.setDisplayOrder(
            rs.getInt(
                "pduct_display_order"
            )
        );

        if ("SIM_ESIM".equals(
                product.getType())) {

            SimProduct sim =
                new SimProduct();

            sim.setProductId(
                product.getId()
            );

            sim.setForm(
                rs.getString(
                    "simprod_form"
                )
            );

            sim.setOperator(
                rs.getString(
                    "simprod_operator"
                )
            );

            sim.setCountryRegion(
                rs.getString(
                    "simprod_country_region"
                )
            );

            sim.setRealnameRequired(
                rs.getInt(
                    "simprod_realname_required"
                )
            );

            sim.setOfficialUrl(
                rs.getString(
                    "simprod_official_url"
                )
            );

            sim.setApplicationGuide(
                rs.getString(
                    "simprod_application_guide"
                )
            );

            sim.setKeepaliveGuide(
                rs.getString(
                    "simprod_keepalive_guide"
                )
            );

            product.setSimProduct(sim);
        }

        return product;
    }
}
