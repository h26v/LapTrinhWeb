<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<title>Đặt lại mật khẩu</title>
<section class="card" style="max-width: 520px; margin: 0 auto;">
    <h1>Đặt lại mật khẩu</h1>
    <p>Nhập OTP đã gửi đến email <strong><c:out value="${sessionScope.resetEmail}"/></strong> và mật khẩu mới.</p>
    <c:if test="${not empty alert}">
        <p class="alert alert-error" role="alert"><c:out value="${alert}"/></p>
    </c:if>
    <form action="<c:url value='/session/reset-password'/>" method="post">
        <label for="otp">Mã OTP</label>
        <input type="text" id="otp" name="otp" required maxlength="6" pattern="[0-9]{6}"
               style="width:100%;padding:10px;border:1px solid #c8d3e2;border-radius:6px;"/>

        <label for="password">Mật khẩu mới</label>
        <input type="password" id="password" name="password" required minlength="6"
               autocomplete="new-password"
               style="width:100%;padding:10px;border:1px solid #c8d3e2;border-radius:6px;"/>

        <label for="confirmPassword">Xác nhận mật khẩu mới</label>
        <input type="password" id="confirmPassword" name="confirmPassword" required minlength="6"
               autocomplete="new-password"
               style="width:100%;padding:10px;border:1px solid #c8d3e2;border-radius:6px;"/>

        <div style="margin-top:16px;"><button type="submit">Đặt lại mật khẩu</button></div>
    </form>
</section>
