<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<title>Trang chủ BT3</title>
<section class="card">
    <h1>BT3 · User Profile</h1>
    <p>Quản lý thông tin cá nhân bằng JPA/Hibernate và upload ảnh multipart.</p>
    <p>
        <a href="<c:url value='/session/login'/>">Đăng nhập Session</a>
        · <a href="<c:url value='/cookie/login'/>">Đăng nhập Cookie</a>
        · <a href="<c:url value='/admin/categories'/>">CRUD Category</a>
    </p>
</section>
