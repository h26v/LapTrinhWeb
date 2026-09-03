<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<title>Xin chào</title>
<section class="card">
    <h1>Xin chào <c:out value="${username}"/></h1>
    <p>Bạn đã đăng nhập bằng Cookie.</p>
    <a href="<c:url value='/cookie/logout'/>">Đăng xuất</a>
</section>
