<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Không có quyền truy cập" scope="request"/>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>

<section class="card border-0 mx-auto text-center" style="max-width: 620px;">
    <div class="card-body p-5">
        <h1 class="h3 mb-3">Không có quyền truy cập</h1>
        <p class="text-muted">
            Khu vực quản trị chỉ dành cho tài khoản có vai trò <strong>admin</strong>.
        </p>
        <div class="d-flex justify-content-center gap-2 mt-4">
            <a class="btn btn-primary" href="<c:url value='/home'/>">Về trang chủ</a>
            <a class="btn btn-outline-secondary" href="<c:url value='/logout'/>">Đăng xuất</a>
        </div>
    </div>
</section>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
