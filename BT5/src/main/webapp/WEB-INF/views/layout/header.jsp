<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${empty pageTitle ? 'BT5 - Spring Boot 4 JSP/JSTL' : pageTitle}"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background: #f4f7fb; }
        main { min-height: calc(100vh - 155px); }
        .navbar-brand { font-weight: 700; letter-spacing: .03em; }
        .card { box-shadow: 0 8px 24px rgba(23, 32, 51, .06); }
        .product-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(220px, 1fr)); gap: 1.25rem; }
        .product-card { overflow: hidden; height: 100%; }
        .product-card a { color: inherit; text-decoration: none; }
        .product-card img, .product-placeholder { width: 100%; aspect-ratio: 4 / 3; object-fit: cover; background: #eef3f9; display: flex; align-items: center; justify-content: center; color: #69758a; }
        .product-card h2 { font-size: 1.05rem; margin: 1rem 1rem .5rem; }
        .product-card p { margin: .35rem 1rem 1rem; }
        .product-detail { display: grid; grid-template-columns: minmax(0, 1fr) minmax(0, 1fr); gap: 1.75rem; }
        .product-detail img, .product-detail .product-placeholder { width: 100%; border-radius: .75rem; object-fit: cover; aspect-ratio: 4 / 3; }
        .price { font-size: 1.4rem; color: #dc3545; font-weight: 700; }
        img.icon { width: 56px; height: 56px; object-fit: cover; border-radius: .5rem; }
        img.avatar-sm { width: 44px; height: 44px; object-fit: cover; border-radius: 50%; }
        .avatar-empty-sm { width: 44px; height: 44px; border-radius: 50%; display: grid; place-items: center; background: #eef3f9; color: #69758a; font-size: .7rem; }
        @media (max-width: 760px) { .product-detail { grid-template-columns: 1fr; } }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="<c:url value='/home'/>">BT5</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNavbar"
                aria-controls="mainNavbar" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="mainNavbar">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item"><a class="nav-link" href="<c:url value='/home'/>">Trang chủ</a></li>
                <li class="nav-item"><a class="nav-link" href="<c:url value='/product'/>">Sản phẩm</a></li>
                <%-- Khu vực quản trị chỉ hiện với tài khoản admin (roleid = 1). --%>
                <c:if test="${sessionScope.account.roleid == 1}">
                    <li class="nav-item"><a class="nav-link" href="<c:url value='/admin/categories'/>">Admin Category</a></li>
                    <li class="nav-item"><a class="nav-link" href="<c:url value='/admin/users'/>">Admin User</a></li>
                    <li class="nav-item"><a class="nav-link" href="<c:url value='/admin/products'/>">Admin Product</a></li>
                </c:if>
            </ul>
            <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                <c:choose>
                    <c:when test="${not empty sessionScope.account}">
                        <li class="nav-item">
                            <span class="navbar-text text-light me-3">
                                <c:out value="${sessionScope.account.userName}"/>
                                <c:if test="${sessionScope.account.roleid == 1}"> (admin)</c:if>
                            </span>
                        </li>
                        <li class="nav-item"><a class="nav-link" href="<c:url value='/logout'/>">Đăng xuất</a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item"><a class="nav-link" href="<c:url value='/login'/>">Đăng nhập</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>

<main class="container py-4">
