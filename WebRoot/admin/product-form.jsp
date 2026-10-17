<%@ page language="java"
         import="com.chouwansim.entity.Product"
         import="com.chouwansim.entity.SimProduct"
         pageEncoding="UTF-8"%>

<%!
    private String html(String text) {

        if (text == null) {
            return "";
        }

        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;");
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


    Product formProduct =
        (Product)
        request.getAttribute(
            "formProduct"
        );


    boolean editMode =
        editProduct != null;


    Product currentProduct =
        editProduct != null
        ? editProduct
        : formProduct;


    SimProduct editSim =
        null;


    if (currentProduct != null) {

        editSim =
            currentProduct
            .getSimProduct();
    }


    String productType =
        currentProduct != null
        && currentProduct.getType()
           != null
        ? currentProduct.getType()
        : "SIM_ESIM";


    String simForm =
        editSim != null
        && editSim.getForm()
           != null
        ? editSim.getForm()
        : "ESIM";


    int realnameRequired =
        editSim != null
        ? editSim
          .getRealnameRequired()
        : 0;


    String officialUrl =
        editSim != null
        ? html(
            editSim.getOfficialUrl()
          )
        : "";


    String applicationGuide =
        editSim != null
        ? html(
            editSim
            .getApplicationGuide()
          )
        : "";


    String keepaliveGuide =
        editSim != null
        ? html(
            editSim
            .getKeepaliveGuide()
          )
        : "";
%>

<!DOCTYPE html>

<html>

<head>

    <title>

        <%=editMode
           ? "编辑商品"
           : "新增商品"%>

        - 臭玩 SIM 管理后台

    </title>


    <meta
        http-equiv="Content-Type"
        content="text/html; charset=UTF-8">


    <link
        rel="stylesheet"
        type="text/css"
        href="<%=path%>/css/admin.css">


    <style type="text/css">

        /*
         * 本页专用样式。
         *
         * 不修改整个 admin.css，
         * 避免影响订单、Wiki、评价等后台页面。
         */

        .form-tip {
            margin-bottom: 16px;
            padding: 10px 12px;

            border-left: 3px solid #8dbbd2;

            background-color: #f5f9fb;

            color: #667784;

            font-size: 12px;
            line-height: 1.8;
        }


        .markdown-group {
            margin-bottom: 22px;
        }


        .markdown-group label {
            display: block;

            margin-bottom: 8px;

            color: #34475a;

            font-size: 14px;
            font-weight: bold;
        }


        .markdown-editor {
            box-sizing: border-box;

            width: 100%;
            min-height: 260px;

            padding: 12px 14px;

            border: 1px solid #bcc7cf;
            border-radius: 0;

            resize: vertical;

            background-color: #ffffff;

            color: #243447;

            font-family:
                Consolas,
                "Courier New",
                monospace;

            font-size: 13px;
            line-height: 1.7;

            outline: none;
        }


        .markdown-editor:focus {
            border-color: #7faec5;
        }


        .markdown-help {
            margin-top: 7px;

            color: #89949e;

            font-size: 11px;
            line-height: 1.7;
        }


        .markdown-example {
            margin-top: 10px;

            padding: 10px 12px;

            border: 1px solid #e1e6ea;

            background-color: #fafbfc;

            color: #65737e;

            font-family:
                Consolas,
                "Courier New",
                monospace;

            font-size: 11px;
            line-height: 1.7;

            white-space: pre-wrap;
        }


        .official-url-input {
            box-sizing: border-box;

            width: 100%;

            height: 38px;

            padding: 0 10px;

            border: 1px solid #bcc7cf;
            border-radius: 0;

            outline: none;
        }


        .official-url-input:focus {
            border-color: #7faec5;
        }


        .save-error {
            margin-bottom: 20px;
            padding: 12px 15px;

            border: 1px solid #e1aaaa;

            background-color: #fff4f4;

            color: #a74444;

            font-size: 14px;
        }

    </style>

</head>


<body>


<div class="admin-shell">


    <div class="admin-header">

        <div class="admin-title">

            臭玩 SIM 管理后台

        </div>

    </div>


    <div class="admin-body">


        <!-- 左侧菜单 -->

        <div class="admin-sidebar">

            <a
                href="<%=path%>/admin/product?action=list"
                class="admin-menu active">

                商品管理

            </a>


            <a
                href="<%=path%>/admin/wiki"
                class="admin-menu">

                Wiki 管理

            </a>


            <a
                href="<%=path%>/admin/orders"
                class="admin-menu">

                订单与物流

            </a>


            <a
                href="<%=path%>/admin/reviews"
                class="admin-menu">

                评价管理

            </a>

        </div>


        <!-- 主体 -->

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

                <div class="save-error">

                    <%=html(saveError)%>

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
                        value="<%=editMode
                            ? "update"
                            : "add"%>">


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


                    <!-- =================================
                         基本资料
                         ================================= -->

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
                                    value="<%=currentProduct != null
                                        ? html(
                                            currentProduct
                                            .getName()
                                          )
                                        : ""%>">

                            </div>


                            <div class="form-group">

                                <label>
                                    商品类型
                                </label>

                                <select
                                    name="type">

                                    <option
                                        value="SIM_ESIM"
                                        <%=
                                            "SIM_ESIM".equals(
                                                productType
                                            )
                                            ? "selected=\"selected\""
                                            : ""
                                        %>>

                                        SIM / eSIM

                                    </option>


                                    <option
                                        value="EUICC"
                                        <%=
                                            "EUICC".equals(
                                                productType
                                            )
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
                                    value="<%=currentProduct != null
                                        ? html(
                                            currentProduct
                                            .getShortDescription()
                                          )
                                        : ""%>">

                            </div>


                            <div class="form-group">

                                <label>
                                    封面图片路径
                                </label>

                                <input
                                    type="text"
                                    name="coverImage"
                                    value="<%=currentProduct != null
                                        ? html(
                                            currentProduct
                                            .getCoverImage()
                                          )
                                        : "images/product/xry.jpg"%>">

                            </div>


                        </div>


                        <div class="form-group full">

                            <label>
                                商品详细介绍
                            </label>

                            <textarea
                                name="description"><%=currentProduct != null
                                    ? html(
                                        currentProduct
                                        .getDescription()
                                      )
                                    : ""%></textarea>

                        </div>


                    </div>


                    <!-- =================================
                         SIM / eSIM 基础资料
                         ================================= -->

                    <div class="form-section">


                        <div class="form-section-title">

                            SIM / eSIM 资料

                        </div>


                        <div class="form-tip">

                            本区域仅用于 SIM / eSIM 商品。
                            如果商品类型选择 eUICC，
                            保存时这些内容不会写入
                            sim_product。

                        </div>


                        <div class="form-row">


                            <div class="form-group">

                                <label>
                                    SIM 形态
                                </label>

                                <select
                                    name="simForm">

                                    <option
                                        value="PHYSICAL_SIM"
                                        <%=
                                            "PHYSICAL_SIM".equals(
                                                simForm
                                            )
                                            ? "selected=\"selected\""
                                            : ""
                                        %>>

                                        实体 SIM

                                    </option>


                                    <option
                                        value="ESIM"
                                        <%=
                                            "ESIM".equals(
                                                simForm
                                            )
                                            ? "selected=\"selected\""
                                            : ""
                                        %>>

                                        eSIM

                                    </option>


                                    <option
                                        value="BOTH"
                                        <%=
                                            "BOTH".equals(
                                                simForm
                                            )
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
                                        ? html(
                                            editSim
                                            .getOperator()
                                          )
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
                                        ? html(
                                            editSim
                                            .getCountryRegion()
                                          )
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


                    <!-- =================================
                         申请与保号规则
                         ================================= -->

                    <div class="form-section">


                        <div class="form-section-title">

                            申请与保号规则

                        </div>


                        <div class="form-tip">

                            详细申请规则和保号规则采用
                            Markdown 原文保存。
                            后台只负责编辑和保存 Markdown；
                            商品详情页后续使用 Markdown
                            渲染器显示。

                        </div>


                        <div class="form-group full">

                            <label>

                                官方申请地址

                            </label>

                            <input
                                type="text"
                                class="official-url-input"
                                name="officialUrl"
                                placeholder="例如：https://www.example.com/apply"
                                value="<%=officialUrl%>">

                        </div>


                        <div class="markdown-group">

                            <label>

                                详细申请规则
                                （Markdown）

                            </label>


                            <textarea
                                class="markdown-editor"
                                name="applicationGuide"
                                placeholder="# 申请条件

## 准备材料

- 邮箱
- 手机号码
- 身份证明

## 申请步骤

1. 打开官方网站
2. 注册账户
3. 填写申请资料
4. 完成付款"><%=applicationGuide%></textarea>


                            <div class="markdown-help">

                                建议使用标题、列表、粗体和链接组织内容，
                                不建议在这里直接写 HTML。

                            </div>


                            <div class="markdown-example"># 申请条件

## 准备材料

- 邮箱
- 手机号码
- 身份证明

## 申请步骤

1. 打开官方网站
2. 注册账户
3. 填写资料
4. 完成申请

**注意：**
部分地区可能要求当地地址。</div>

                        </div>


                        <div class="markdown-group">

                            <label>

                                保号规则
                                （Markdown）

                            </label>


                            <textarea
                                class="markdown-editor"
                                name="keepaliveGuide"
                                placeholder="# 保号规则

## 保号周期

建议每 6 个月完成一次有效消费。

## 保号方式

- 充值
- 发送短信
- 产生通话
- 购买套餐

## 注意事项

**请以运营商最新规则为准。**"><%=keepaliveGuide%></textarea>


                            <div class="markdown-help">

                                这里填写运营商或产品层面的长期保号规则。
                                用户自己设置的保号计划仍由
                                “我的卡片”功能单独管理。

                            </div>


                            <div class="markdown-example"># 保号规则

## 保号周期

建议每 **6 个月** 完成一次有效消费。

## 可用方式

- 充值
- 发送短信
- 拨打电话

## 注意

超过规定周期可能进入停机或号码回收流程。</div>

                        </div>


                    </div>


                    <!-- =================================
                         销售信息
                         ================================= -->

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
                                            currentProduct == null
                                            || currentProduct
                                               .getPurchasable()
                                               == 1
                                            ? "selected=\"selected\""
                                            : ""
                                        %>>

                                        可以购买

                                    </option>


                                    <option
                                        value="0"
                                        <%=
                                            currentProduct != null
                                            && currentProduct
                                               .getPurchasable()
                                               == 0
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
                                    value="<%=currentProduct != null
                                        && currentProduct
                                           .getPrice()
                                           != null
                                        ? currentProduct
                                          .getPrice()
                                          .toString()
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
                                    value="<%=currentProduct != null
                                        && currentProduct
                                           .getStock()
                                           != null
                                        ? currentProduct
                                          .getStock()
                                          .toString()
                                        : ""%>">

                            </div>


                            <div class="form-group">

                                <label>
                                    商品状态
                                </label>

                                <select
                                    name="status">

                                    <option
                                        value="DRAFT"
                                        <%=
                                            currentProduct == null
                                            || "DRAFT".equals(
                                                currentProduct
                                                .getStatus()
                                            )
                                            ? "selected=\"selected\""
                                            : ""
                                        %>>

                                        草稿

                                    </option>


                                    <option
                                        value="PUBLISHED"
                                        <%=
                                            currentProduct != null
                                            && "PUBLISHED".equals(
                                                currentProduct
                                                .getStatus()
                                            )
                                            ? "selected=\"selected\""
                                            : ""
                                        %>>

                                        上架

                                    </option>


                                    <option
                                        value="ARCHIVED"
                                        <%=
                                            currentProduct != null
                                            && "ARCHIVED".equals(
                                                currentProduct
                                                .getStatus()
                                            )
                                            ? "selected=\"selected\""
                                            : ""
                                        %>>

                                        下架 / 归档

                                    </option>

                                </select>

                            </div>


                        </div>


                    </div>


                    <!-- =================================
                         保存
                         ================================= -->

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
