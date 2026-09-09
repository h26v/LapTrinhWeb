<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<title>Xác thực OTP đăng ký</title>
<section class="card" style="max-width: 520px; margin: 0 auto;">
    <h1>Kích hoạt tài khoản</h1>
    <p>Nhập mã OTP đã gửi đến email <strong><c:out value="${sessionScope.activationEmail}"/></strong>.</p>
    <c:if test="${not empty alert}">
        <p class="alert alert-error" role="alert"><c:out value="${alert}"/></p>
    </c:if>
    <c:if test="${not empty success}">
        <p class="alert alert-success"><c:out value="${success}"/></p>
    </c:if>
    <form action="<c:url value='/session/register/verify'/>" method="post">
        <label for="otp">Mã OTP</label>
        <input type="text" id="otp" name="otp" required maxlength="6" pattern="[0-9]{6}"
               style="width:100%;padding:10px;border:1px solid #c8d3e2;border-radius:6px;"/>
        <div style="margin-top:16px;"><button type="submit">Kích hoạt</button></div>
    </form>
    <form action="<c:url value='/session/register/resend-otp'/>" method="post" style="margin-top:12px;">
        <button type="submit">Gửi lại OTP</button>
    </form>
</section>
