<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<title>Xin chào</title>
<section class="card border-0">
    <div class="card-body p-4">
        <h1 class="h3">Xin chào <c:out value="${username}"/></h1>
        <p class="text-muted">Bạn đã đăng nhập bằng Cookie.</p>
        <a class="btn btn-outline-danger" href="<c:url value='/cookie/logout'/>">Đăng xuất</a>
    </div>
</section>
