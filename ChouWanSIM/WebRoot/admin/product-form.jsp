<%@ page language="java"
         import="com.chouwansim.entity.Product"
         import="com.chouwansim.entity.SimProduct"
         pageEncoding="UTF-8"%>

<%!
    private String value(String text) {

        if (text == null) {
            return "";
        }

        return text;
    }
%>

<%
    String path =
        request.getContextPath();

    String saveError =
        (String)
        request.getAttribute(
            "saveError"
        );

    Product editProduct =
        (Product)
        request.getAttribute(
            "editProduct"
        );

    boolean editMode =
        editProduct != null;

    SimProduct editSim =
        null;

    if (editMode) {
        editSim =
            editProduct.getSimProduct();
    }

    String productType =
        editMode
        ? editProduct.getType()
        : "SIM_ESIM";

    String simForm =
        editSim != null
        ? editSim.getForm()
        : "ESIM";

    int realnameRequired =
        editSim != null
        ? editSim.getRealnameRequired()
        : 0;
%>

<!DOCTYPE html>

<html>

<head>

    <title>
        <%=editMode ? "编辑商品" : "新增商品"%>
        - 臭玩 SIM 管理后台
    </title>

    <meta http-equiv="Content-Type"
          content="text/html; charset=UTF-8">

    <link rel="stylesheet"
          type="text/css"
          href="<%=path%>/css/admin.css">

</head>

<body>

<div class="admin-shell">

    <div class="admin-header">

        <div class="admin-title">
            臭玩 SIM 管理后台
        </div>

    </div>

    <div class="admin-body">

        <div class="admin-sidebar">

            <a href="<%=path%>/admin/product?action=list"
               class="admin-menu active">
                商品管理
            </a>

            <a href="<%=path%>/admin/wiki"
               class="admin-menu">
                Wiki 管理
            </a>

            <a href="<%=path%>/admin/orders"
               class="admin-menu">
                订单与物流
            </a>

            <a href="<%=path%>/admin/reviews"
               class="admin-menu">
                评价管理
            </a>

        </div>

        <div class="admin-main">

            <div class="content-header">

                <h1>
                    <%=editMode
                       ? "编辑商品"
                       : "新增商品"%>
                </h1>

            </div>

            <%
                if (saveError != null) {
            %>

                <div style="
                    margin-bottom:20px;
                    padding:12px 15px;
                    border:1px solid #e1aaaa;
                    background-color:#fff4f4;
                    color:#a74444;
                    font-size:14px;
                ">

                    <%=saveError%>

                </div>

            <%
                }
            %>

            <div class="form-panel">

                <form
                    action="<%=path%>/admin/product"
                    method="post">

                    <input
                        type="hidden"
                        name="action"
                        value="<%=editMode ? "update" : "add"%>">

                    <%
                        if (editMode) {
                    %>

                        <input
                            type="hidden"
                            name="id"
                            value="<%=editProduct.getId()%>">

                    <%
                        }
                    %>

                    <div class="form-section">

                        <div class="form-section-title">
                            基本资料
                        </div>

                        <div class="form-row">

                            <div class="form-group">

                                <label>
                                    商品名称
                                </label>

                                <input
                                    type="text"
                                    name="name"
                                    value="<%=editMode
                                        ? value(editProduct.getName())
                                        : ""%>">

                            </div>

                            <div class="form-group">

                                <label>
                                    商品类型
                                </label>

                                <select name="type">

                                    <option
                                        value="SIM_ESIM"
                                        <%=
                                            "SIM_ESIM".equals(
                                                productType)
                                            ? "selected=\"selected\""
                                            : ""
                                        %>>

                                        SIM / eSIM

                                    </option>

                                    <option
                                        value="EUICC"
                                        <%=
                                            "EUICC".equals(
                                                productType)
                                            ? "selected=\"selected\""
                                            : ""
                                        %>>

                                        eUICC

                                    </option>

                                </select>

                            </div>

                        </div>

                        <div class="form-row">

                            <div class="form-group">

                                <label>
                                    简短介绍
                                </label>

                                <input
                                    type="text"
                                    name="shortDescription"
                                    value="<%=editMode
                                        ? value(editProduct.getShortDescription())
                                        : ""%>">

                            </div>

                            <div class="form-group">

                                <label>
                                    封面图片路径
                                </label>

                                <input
                                    type="text"
                                    name="coverImage"
                                    value="<%=editMode
                                        ? value(editProduct.getCoverImage())
                                        : "images/product/xry.jpg"%>">

                            </div>

                        </div>

                        <div class="form-group full">

                            <label>
                                商品详细介绍
                            </label>

                            <textarea
                                name="description"><%=editMode
                                    ? value(editProduct.getDescription())
                                    : ""%></textarea>

                        </div>

                    </div>

                    <div class="form-section">

                        <div class="form-section-title">
                            SIM / eSIM 资料
                        </div>

                        <div style="
                            margin-bottom:15px;
                            color:#777777;
                            font-size:12px;
                        ">
                            如果商品类型选择 eUICC，
                            本区域内容会被后台忽略。
                        </div>

                        <div class="form-row">

                            <div class="form-group">

                                <label>
                                    SIM 形态
                                </label>

                                <select name="simForm">

                                    <option
                                        value="PHYSICAL_SIM"
                                        <%=
                                            "PHYSICAL_SIM".equals(
                                                simForm)
                                            ? "selected=\"selected\""
                                            : ""
                                        %>>

                                        实体 SIM

                                    </option>

                                    <option
                                        value="ESIM"
                                        <%=
                                            "ESIM".equals(
                                                simForm)
                                            ? "selected=\"selected\""
                                            : ""
                                        %>>

                                        eSIM

                                    </option>

                                    <option
                                        value="BOTH"
                                        <%=
                                            "BOTH".equals(
                                                simForm)
                                            ? "selected=\"selected\""
                                            : ""
                                        %>>

                                        两者均支持

                                    </option>

                                </select>

                            </div>

                            <div class="form-group">

                                <label>
                                    运营商 / 品牌
                                </label>

                                <input
                                    type="text"
                                    name="operator"
                                    value="<%=editSim != null
                                        ? value(editSim.getOperator())
                                        : ""%>">

                            </div>

                        </div>

                        <div class="form-row">

                            <div class="form-group">

                                <label>
                                    国家 / 地区
                                </label>

                                <input
                                    type="text"
                                    name="countryRegion"
                                    value="<%=editSim != null
                                        ? value(editSim.getCountryRegion())
                                        : ""%>">

                            </div>

                            <div class="form-group">

                                <label>
                                    实名要求
                                </label>

                                <select
                                    name="realnameRequired">

                                    <option
                                        value="0"
                                        <%=
                                            realnameRequired == 0
                                            ? "selected=\"selected\""
                                            : ""
                                        %>>

                                        不需要

                                    </option>

                                    <option
                                        value="1"
                                        <%=
                                            realnameRequired == 1
                                            ? "selected=\"selected\""
                                            : ""
                                        %>>

                                        需要

                                    </option>

                                </select>

                            </div>

                        </div>

                    </div>

                    <div class="form-section">

                        <div class="form-section-title">
                            销售信息
                        </div>

                        <div class="form-row">

                            <div class="form-group">

                                <label>
                                    本站是否可购买
                                </label>

                                <select
                                    name="purchasable">

                                    <option
                                        value="1"
                                        <%=
                                            !editMode
                                            || editProduct.getPurchasable() == 1
                                            ? "selected=\"selected\""
                                            : ""
                                        %>>

                                        可以购买

                                    </option>

                                    <option
                                        value="0"
                                        <%=
                                            editMode
                                            && editProduct.getPurchasable() == 0
                                            ? "selected=\"selected\""
                                            : ""
                                        %>>

                                        仅展示 / 自行申请

                                    </option>

                                </select>

                            </div>

                            <div class="form-group">

                                <label>
                                    价格
                                </label>

                                <input
                                    type="text"
                                    name="price"
                                    placeholder="例如：128.00"
                                    value="<%=editMode
                                        && editProduct.getPrice() != null
                                        ? editProduct.getPrice().toString()
                                        : ""%>">

                            </div>

                        </div>

                        <div class="form-row">

                            <div class="form-group">

                                <label>
                                    库存
                                </label>

                                <input
                                    type="text"
                                    name="stock"
                                    placeholder="例如：20"
                                    value="<%=editMode
                                        && editProduct.getStock() != null
                                        ? editProduct.getStock().toString()
                                        : ""%>">

                            </div>

                            <div class="form-group">

                                <label>
                                    商品状态
                                </label>

                                <select name="status">

                                    <option
                                        value="DRAFT"
                                        <%=
                                            editMode
                                            && "DRAFT".equals(
                                                editProduct.getStatus())
                                            ? "selected=\"selected\""
                                            : ""
                                        %>>

                                        草稿

                                    </option>

                                    <option
                                        value="PUBLISHED"
                                        <%=
                                            editMode
                                            && "PUBLISHED".equals(
                                                editProduct.getStatus())
                                            ? "selected=\"selected\""
                                            : ""
                                        %>>

                                        上架

                                    </option>

                                    <option
                                        value="ARCHIVED"
                                        <%=
                                            editMode
                                            && "ARCHIVED".equals(
                                                editProduct.getStatus())
                                            ? "selected=\"selected\""
                                            : ""
                                        %>>

                                        下架 / 归档

                                    </option>

                                </select>

                            </div>

                        </div>

                    </div>

                    <div class="form-actions">

                        <a
                            href="<%=path%>/admin/product?action=list"
                            class="cancel-button">

                            取消

                        </a>

                        <button
                            type="submit"
                            class="save-button">

                            <%=editMode
                               ? "保存修改"
                               : "保存商品"%>

                        </button>

                    </div>

                </form>

            </div>

        </div>

    </div>

</div>

</body>

</html>
