<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<title>Quên mật khẩu</title>
<section class="card" style="max-width: 520px; margin: 0 auto;">
    <h1>Quên mật khẩu</h1>
    <p>Nhập email tài khoản để nhận mã OTP đặt lại mật khẩu.</p>
    <c:if test="${not empty alert}">
        <p class="alert alert-error" role="alert"><c:out value="${alert}"/></p>
    </c:if>
    <c:if test="${not empty success}">
        <p class="alert alert-success"><c:out value="${success}"/></p>
    </c:if>
    <form action="<c:url value='/session/forgot-password'/>" method="post">
        <label for="email">Email</label>
        <input type="email" id="email" name="email" required autocomplete="email"
               style="width:100%;padding:10px;border:1px solid #c8d3e2;border-radius:6px;"/>
        <div style="margin-top:16px;"><button type="submit">Gửi OTP</button></div>
    </form>
    <p><a href="<c:url value='/session/login'/>">Quay lại đăng nhập</a></p>
</section>
