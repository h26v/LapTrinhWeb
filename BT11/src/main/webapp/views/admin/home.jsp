<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Dashboard</title></head>
<body>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<h3 class="mb-4">Trang quản trị</h3>
<div class="row g-3">
    <div class="col-sm-6 col-lg-3">
        <a class="card text-bg-primary text-decoration-none" href="${ctx}/admin/books">
            <div class="card-body"><div class="fs-2 fw-bold">${bookCount}</div>Sách</div>
        </a>
    </div>
    <div class="col-sm-6 col-lg-3">
        <a class="card text-bg-success text-decoration-none" href="${ctx}/admin/authors">
            <div class="card-body"><div class="fs-2 fw-bold">${authorCount}</div>Tác giả</div>
        </a>
    </div>
    <div class="col-sm-6 col-lg-3">
        <div class="card text-bg-warning">
            <div class="card-body"><div class="fs-2 fw-bold">${userCount}</div>Người dùng</div>
        </div>
    </div>
    <div class="col-sm-6 col-lg-3">
        <div class="card text-bg-info">
            <div class="card-body"><div class="fs-2 fw-bold">${reviewCount}</div>Reviews</div>
        </div>
    </div>
</div>
</body>
</html>
