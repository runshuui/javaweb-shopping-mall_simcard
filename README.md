# JavaWeb 购物商城 —— SIM 卡选购平台

这是一个基于传统 Java Web 技术栈开发的 SIM / eSIM / eUICC 购物与信息平台，也是当前 Java Web 课程项目。

项目以 SIM / eSIM 商品信息和选卡体验为入口，逐步整合商品浏览、申请说明、购物车、结算、模拟支付、订单、物流、六维评价、个人持卡管理、保号管理和 Wiki 等功能。目前项目已经形成“商品 → 购物车 → 结算 → 支付 → 订单 → 物流 → 我的卡片”的主要业务链路，并继续完善 Wiki 后台和页面细节。

## 当前开发环境

| 软件 / 技术 | 当前版本 / 用途 |
| --- | --- |
| MyEclipse | MyEclipse 10 |
| JDK | JDK 1.6 |
| Tomcat | Tomcat 6 |
| MySQL | MySQL 5.1.38-community |
| 数据库 | `chouwansim` |
| JDBC Driver | MySQL Connector/J 5.1.x |
| 后端 | Servlet / Service / DAO / JDBC |
| 前端 | JSP / HTML / CSS / JavaScript |
| 登录状态 | HttpSession |
| 权限控制 | Filter |
| 数据库字符集 | UTF-8 |

JDBC 连接继续使用 UTF-8，例如：

```text
useUnicode=true&characterEncoding=UTF-8
```

> 数据库账号、密码等本机敏感配置不提交到公开仓库。

## 当前数据库

当前 `chouwansim` 数据库已经建立 15 张主要业务表：

```text
card_keepalive
logistics
order_address
order_item
orders
payment_record
product
shopping_cart
sim_product
sim_review
user_account
user_address
user_card
wiki_article
wiki_category
```

## 当前项目目录

```text
ChouWanSIM
├─ src
│  ├─ com.chouwansim.dao
│  │  ├─ ProductDao.java
│  │  ├─ UserAccountDao.java
│  │  ├─ ShoppingCartDao.java
│  │  ├─ UserAddressDao.java
│  │  ├─ OrderDao.java
│  │  ├─ OrderQueryDao.java
│  │  ├─ AdminOrderDao.java
│  │  ├─ SimReviewDao.java
│  │  ├─ UserCardDao.java
│  │  ├─ CardKeepaliveDao.java
│  │  ├─ WikiCategoryDao.java
│  │  └─ WikiArticleDao.java
│  │
│  ├─ com.chouwansim.entity
│  │  ├─ Product.java
│  │  ├─ SimProduct.java
│  │  ├─ UserAccount.java
│  │  ├─ CartItem.java
│  │  ├─ UserAddress.java
│  │  ├─ OrderResult.java
│  │  ├─ Order.java
│  │  ├─ OrderItem.java
│  │  ├─ SimReview.java
│  │  ├─ ReviewSummary.java
│  │  ├─ UserCard.java
│  │  ├─ CardKeepalive.java
│  │  ├─ WikiCategory.java
│  │  └─ WikiArticle.java
│  │
│  ├─ com.chouwansim.filter
│  │  └─ AdminFilter.java
│  │
│  ├─ com.chouwansim.service
│  │  ├─ LoginService.java
│  │  ├─ ProductService.java
│  │  ├─ ShoppingCartService.java
│  │  ├─ UserAddressService.java
│  │  ├─ OrderService.java
│  │  ├─ OrderQueryService.java
│  │  ├─ AdminOrderService.java
│  │  ├─ SimReviewService.java
│  │  ├─ UserCardService.java
│  │  └─ WikiService.java
│  │
│  ├─ com.chouwansim.servlet
│  │  ├─ LoginServlet.java
│  │  ├─ AdminProductServlet.java
│  │  ├─ StoreProductServlet.java
│  │  ├─ ProductDetailServlet.java
│  │  ├─ CartServlet.java
│  │  ├─ UserAddressServlet.java
│  │  ├─ CheckoutServlet.java
│  │  ├─ OrderPaymentServlet.java
│  │  ├─ UserOrderServlet.java
│  │  ├─ OrderDetailServlet.java
│  │  ├─ AdminOrderServlet.java
│  │  ├─ UserCenterServlet.java
│  │  ├─ LogoutServlet.java
│  │  ├─ UserReviewServlet.java
│  │  ├─ AdminReviewServlet.java
│  │  ├─ UserCardServlet.java
│  │  ├─ WikiServlet.java
│  │  ├─ WikiArticleServlet.java
│  │  └─ AdminWikiServlet.java
│  │
│  └─ com.chouwansim.util
│     ├─ DBUtil.java
│     ├─ PasswordUtil.java
│     └─ MarkdownUtil.java
│
└─ WebRoot
   ├─ login.jsp
   ├─ index.jsp
   ├─ euicc.jsp
   ├─ wiki.jsp
   ├─ wiki-article.jsp
   ├─ my-cards.jsp
   ├─ cart.jsp
   ├─ user-center.jsp
   ├─ user-address.jsp
   ├─ checkout.jsp
   ├─ mobile-pay.jsp
   ├─ usdt-pay.jsp
   ├─ payment-success.jsp
   ├─ orders.jsp
   ├─ order-detail.jsp
   ├─ reviews.jsp
   ├─ review-form.jsp
   ├─ product-detail.jsp
   ├─ admin
   │  ├─ index.jsp
   │  ├─ product-form.jsp
   │  ├─ orders.jsp
   │  ├─ order-detail.jsp
   │  ├─ reviews.jsp
   │  ├─ wiki.jsp
   │  └─ wiki-form.jsp
   ├─ css
   │  ├─ login.css
   │  ├─ index.css
   │  ├─ admin.css
   │  └─ product-detail.css
   ├─ images
   │  ├─ common
   │  ├─ login
   │  ├─ product
   │  │  └─ xry.jpg
   │  └─ checkout
   └─ WEB-INF
      ├─ lib
      │  └─ mysql-connector-java-5.1.x.jar
      └─ web.xml
```

## 目录与文件职责

### `com.chouwansim.entity`

业务实体层，用于 DAO、Service、Servlet 与 JSP 之间的数据传递。

- `Product.java`：商城公共商品信息。
- `SimProduct.java`：SIM / eSIM 专属商品资料。
- `UserAccount.java`：用户账户信息。
- `CartItem.java`：购物车商品展示模型。
- `UserAddress.java`：用户收货地址。
- `Order.java` / `OrderItem.java`：订单主信息与商品明细。
- `OrderResult.java`：下单事务完成后的结果对象。
- `SimReview.java` / `ReviewSummary.java`：六维评价及评价汇总。
- `UserCard.java`：用户实际持有的 SIM / eSIM。
- `CardKeepalive.java`：卡片保号计划。
- `WikiCategory.java` / `WikiArticle.java`：Wiki 分类与文章。

### `com.chouwansim.dao`

数据访问层，统一使用 JDBC 和 `PreparedStatement` 与 MySQL 交互。

主要负责用户、商品、购物车、地址、订单、物流、评价、用户卡片、保号计划以及 Wiki 的查询和增删改操作。

### `com.chouwansim.service`

业务逻辑层。

- `LoginService.java`：登录验证。
- `ProductService.java`：商品 CRUD、上下架、商城筛选与详情。
- `ShoppingCartService.java`：购物车业务。
- `UserAddressService.java`：收货地址业务。
- `OrderService.java`：下单事务、扣库存、清购物车，并在购买 SIM / eSIM 后生成持卡记录。
- `OrderQueryService.java`：用户订单查询。
- `AdminOrderService.java`：后台订单及发货业务。
- `SimReviewService.java`：评价写入、修改、汇总。
- `UserCardService.java`：我的卡片、保号日期计算和状态管理。
- `WikiService.java`：Wiki 分类和文章业务校验。

### `com.chouwansim.servlet`

请求控制层，负责接收请求参数、调用 Service，并决定 JSP 跳转。

主要入口：

```text
/login              → LoginServlet
/admin/product      → AdminProductServlet
/store              → StoreProductServlet
/product            → ProductDetailServlet
/cart               → CartServlet
/address            → UserAddressServlet
/checkout           → CheckoutServlet
/order/pay          → OrderPaymentServlet
/orders             → UserOrderServlet
/order/detail       → OrderDetailServlet
/admin/orders       → AdminOrderServlet
/user-center        → UserCenterServlet
/logout             → LogoutServlet
/reviews            → UserReviewServlet
/admin/reviews      → AdminReviewServlet
/my-cards           → UserCardServlet
/wiki               → WikiServlet
/wiki/article       → WikiArticleServlet
/admin/wiki         → AdminWikiServlet
```

### `com.chouwansim.filter`

- `AdminFilter.java`：保护 `/admin/*`，只有管理员可以进入后台。

### `com.chouwansim.util`

- `DBUtil.java`：数据库连接与 JDBC 资源释放。
- `PasswordUtil.java`：PBKDF2WithHmacSHA1 密码验证。
- `MarkdownUtil.java`：JDK 1.6 兼容的轻量 Markdown 转 HTML 工具，原始 HTML 默认转义。

## WebRoot 页面职责

### 商城与用户端

- `login.jsp`：登录页。
- `index.jsp`：SIM / eSIM 商城首页。
- `euicc.jsp`：eUICC 商品页。
- `product-detail.jsp`：商品详情、六维评分、文字评价与雷达图。
- `cart.jsp`：购物车。
- `user-address.jsp`：收货地址管理。
- `checkout.jsp`：结算页。
- `mobile-pay.jsp` / `usdt-pay.jsp`：模拟支付页面。
- `payment-success.jsp`：支付成功页。
- `orders.jsp` / `order-detail.jsp`：用户订单列表和详情。
- `user-center.jsp`：用户中心。
- `reviews.jsp` / `review-form.jsp`：用户评价。
- `my-cards.jsp`：我的卡片及保号信息。
- `wiki.jsp` / `wiki-article.jsp`：Wiki 首页和文章详情。

### 管理后台

- `admin/index.jsp`：后台商品管理主页。
- `admin/product-form.jsp`：商品新增 / 编辑。
- `admin/orders.jsp` / `admin/order-detail.jsp`：后台订单和物流。
- `admin/reviews.jsp`：评价管理。
- `admin/wiki.jsp` / `admin/wiki-form.jsp`：Wiki 分类、文章和 Markdown 编辑。

## 当前主要业务链路

### 商城订单

```text
登录
→ 商品列表
→ 商品详情
→ 加入购物车
→ 收货地址
→ 结算
→ 模拟支付
→ 创建订单
→ 扣减库存
→ 清购物车
→ 我的订单
→ 管理员发货
→ 用户查看物流
```

### 我的卡片

```text
购买 SIM / eSIM
→ 支付成功
→ 自动生成 user_card
→ 我的卡片
→ 编辑卡片
→ 设置保号周期
→ 计算下一次保号日期
→ 记录本次保号
```

用户也可以手动添加站外获得的 SIM / eSIM。

### 评价

```text
用户提交六维评分
→ sim_review
→ AVG / COUNT 汇总
→ 商品详情
→ 雷达图 + 文字评论
```

### Wiki

```text
wiki_category
→ wiki_article
→ Markdown
→ HTML
→ 前台展示
```

Wiki 前台基础链路已经建立，管理后台仍在继续排查和完善。

## 当前开发重点

下一阶段主要继续处理：

- Wiki 后台路由和文章 CRUD。
- 商城和商品详情页 UI 细节。
- 选卡器交互。
- 用户中心与页面导航统一。
- 异常处理和安全细节。

## 项目说明

本项目主要用于 Java Web 课程学习与展示。

商城中的支付、物流以及部分 SIM / eSIM 产品数据属于课程项目中的模拟或演示功能，不代表真实运营商服务。
