<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><sitemesh:write property="title">BT4 - SiteMesh Bootstrap</sitemesh:write></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <sitemesh:write property="head" />
    <style>
        body { background: #f4f7fb; }
        main { min-height: calc(100vh - 155px); }
        .navbar-brand { font-weight: 700; letter-spacing: .03em; }
        .card { box-shadow: 0 8px 24px rgba(23, 32, 51, .06); }
        .alert-error { --bs-alert-color: #842029; --bs-alert-bg: #f8d7da; --bs-alert-border-color: #f5c2c7; }
        .alert-success { --bs-alert-color: #0f5132; --bs-alert-bg: #d1e7dd; --bs-alert-border-color: #badbcc; }
        .product-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(220px, 1fr)); gap: 1.25rem; }
        .product-card { overflow: hidden; height: 100%; }
        .product-card a { color: inherit; text-decoration: none; }
        .product-card img, .product-placeholder { width: 100%; aspect-ratio: 4 / 3; object-fit: cover; background: #eef3f9; display: flex; align-items: center; justify-content: center; color: #69758a; }
        .product-card h2 { font-size: 1.05rem; margin: 1rem 1rem .5rem; }
        .product-card p { margin: .35rem 1rem 1rem; }
        .product-detail { display: grid; grid-template-columns: minmax(0, 1fr) minmax(0, 1fr); gap: 1.75rem; }
        .product-detail img { width: 100%; border-radius: .75rem; object-fit: cover; }
        .price { font-size: 1.4rem; color: #dc3545; font-weight: 700; }
        img.icon { width: 56px; height: 56px; object-fit: cover; border-radius: .5rem; }
        .avatar { width: 180px; height: 180px; border-radius: 50%; object-fit: cover; border: 1px solid #dce4ef; background: #eef3f9; }
        .avatar-empty { width: 180px; height: 180px; border-radius: 50%; display: grid; place-items: center; background: #eef3f9; color: #69758a; text-align: center; padding: 1rem; }
        @media (max-width: 760px) { .product-detail { grid-template-columns: 1fr; } }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="<c:url value='/home'/>">BT4</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNavbar"
                    aria-controls="mainNavbar" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="mainNavbar">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item"><a class="nav-link" href="<c:url value='/home'/>">Trang chủ</a></li>
                    <li class="nav-item"><a class="nav-link" href="<c:url value='/product'/>">Sản phẩm</a></li>
                    <li class="nav-item"><a class="nav-link" href="<c:url value='/admin/categories'/>">Admin Category</a></li>
                    <li class="nav-item"><a class="nav-link" href="<c:url value='/admin/products'/>">Admin Product</a></li>
                </ul>
                <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                    <c:choose>
                        <c:when test="${not empty sessionScope.account}">
                            <li class="nav-item"><a class="nav-link" href="<c:url value='/session/profile'/>">Profile</a></li>
                            <li class="nav-item"><a class="nav-link" href="<c:url value='/session/logout'/>">Đăng xuất</a></li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item"><a class="nav-link" href="<c:url value='/session/login'/>">Đăng nhập</a></li>
                            <li class="nav-item"><a class="nav-link" href="<c:url value='/session/register'/>">Đăng ký</a></li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>

    <main class="container py-4">
        <sitemesh:write property="body">Nội dung đang được tải.</sitemesh:write>
    </main>

    <footer class="border-top bg-white py-3">
        <div class="container text-muted small">BT4 · SiteMesh Decorator 3 · Bootstrap · JPA / Hibernate</div>
    </footer>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
