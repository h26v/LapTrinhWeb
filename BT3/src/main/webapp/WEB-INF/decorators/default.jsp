<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><sitemesh:write property="title">BT3 - User Profile</sitemesh:write></title>
    <sitemesh:write property="head" />
    <style>
        :root { color-scheme: light; font-family: Arial, sans-serif; }
        * { box-sizing: border-box; }
        body { margin: 0; background: #f4f7fb; color: #172033; }
        label { display: block; margin: 12px 0 6px; font-weight: 600; }
        button, input, select, textarea { font: inherit; }
        button { padding: 9px 14px; border: 0; border-radius: 6px; background: #1f5eff; color: #fff; cursor: pointer; }
        button:hover { background: #174bd0; }
        a { color: #1f5eff; }
        .topbar { background: #172033; color: #fff; }
        .nav { max-width: 1100px; margin: 0 auto; padding: 16px 24px; display: flex;
               align-items: center; justify-content: space-between; gap: 20px; }
        .brand { color: #fff; font-weight: 700; text-decoration: none; letter-spacing: .04em; }
        .nav-links { display: flex; gap: 16px; flex-wrap: wrap; }
        .nav-links a { color: #d9e4f3; text-decoration: none; }
        .nav-links a:hover { color: #fff; }
        main { max-width: 1100px; margin: 36px auto; padding: 0 24px; min-height: calc(100vh - 150px); }
        .card { background: #fff; border: 1px solid #dce4ef; border-radius: 12px; padding: 28px;
                box-shadow: 0 8px 24px rgba(23, 32, 51, .06); }
        .footer { max-width: 1100px; margin: 0 auto; padding: 20px 24px 32px; color: #69758a; font-size: .9rem; }
        .alert { padding: 10px 12px; border-radius: 8px; }
        .alert-error { background: #fff0f0; color: #9f1d1d; border: 1px solid #f5b9b9; }
        .alert-success { background: #eefaf1; color: #176b2c; border: 1px solid #bde7c6; }
        .muted { color: #69758a; }
        .product-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(220px, 1fr)); gap: 18px; margin-top: 20px; }
        .product-card { border: 1px solid #dce4ef; border-radius: 12px; overflow: hidden; background: #fff; }
        .product-card a { color: inherit; text-decoration: none; }
        .product-card img, .product-placeholder { width: 100%; aspect-ratio: 4 / 3; object-fit: cover; background: #eef3f9; display: flex; align-items: center; justify-content: center; color: #69758a; }
        .product-card h2 { font-size: 1.05rem; margin: 12px 12px 6px; }
        .product-card p { margin: 6px 12px 12px; }
        .product-detail { display: grid; grid-template-columns: minmax(0, 1fr) minmax(0, 1fr); gap: 28px; }
        .product-detail img { width: 100%; border-radius: 12px; object-fit: cover; }
        .price { font-size: 1.4rem; color: #d62828; font-weight: 700; }
        .pagination { margin-top: 22px; display: flex; gap: 8px; flex-wrap: wrap; }
        .pagination a, .pagination strong { padding: 8px 12px; border: 1px solid #dce4ef; border-radius: 6px; text-decoration: none; }
        @media (max-width: 760px) { .product-detail { grid-template-columns: 1fr; } }
    </style>
</head>
<body>
    <header class="topbar">
        <nav class="nav">
            <a class="brand" href="<c:url value='/home'/>">BT3 · PROFILE</a>
            <div class="nav-links">
                <a href="<c:url value='/home'/>">Trang chủ</a>
                <a href="<c:url value='/product'/>">Sản phẩm</a>
                <a href="<c:url value='/admin/categories'/>">Admin Category</a>
                <a href="<c:url value='/admin/products'/>">Admin Product</a>
                <c:choose>
                    <c:when test="${not empty sessionScope.account}">
                        <a href="<c:url value='/session/profile'/>">Profile</a>
                        <a href="<c:url value='/session/logout'/>">Đăng xuất</a>
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='/session/login'/>">Đăng nhập</a>
                        <a href="<c:url value='/session/register'/>">Đăng ký</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </nav>
    </header>
    <main>
        <sitemesh:write property="body">Nội dung đang được tải.</sitemesh:write>
    </main>
    <footer class="footer">BT3 · JPA / Hibernate · SiteMesh</footer>
</body>
</html>
