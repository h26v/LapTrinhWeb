<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><sitemesh:write property="title"/> | Admin BookStore</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <style>
        body { min-height: 100vh; display: flex; flex-direction: column; }
        main { flex: 1; }
        .admin-sidebar .nav-link { color: #212529; }
        .admin-sidebar .nav-link:hover { background: #e9ecef; }
        .thumb { width: 48px; height: 64px; object-fit: cover; }
    </style>
    <sitemesh:write property="head"/>
</head>
<body>
<%-- Header admin: cung menu nhung mau toi de phan biet vai tro --%>
<c:set var="navClass" value="navbar-dark bg-dark"/>
<%@ include file="/WEB-INF/includes/header.jspf" %>

<main class="container-fluid py-4">
    <div class="row">
        <aside class="col-md-2 mb-3">
            <div class="list-group admin-sidebar">
                <span class="list-group-item active fw-bold"><i class="bi bi-speedometer2"></i> Quản trị</span>
                <a class="list-group-item list-group-item-action" href="${pageContext.request.contextPath}/admin/home">
                    <i class="bi bi-house"></i> Dashboard</a>
                <a class="list-group-item list-group-item-action" href="${pageContext.request.contextPath}/admin/books">
                    <i class="bi bi-journal-bookmark"></i> Quản lý Sách</a>
                <a class="list-group-item list-group-item-action" href="${pageContext.request.contextPath}/admin/authors">
                    <i class="bi bi-people"></i> Quản lý Tác giả</a>
            </div>
        </aside>
        <section class="col-md-10">
            <sitemesh:write property="body"/>
        </section>
    </div>
</main>

<%@ include file="/WEB-INF/includes/footer.jspf" %>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
