<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<title>Xác thực OTP đăng ký</title>
<section class="card border-0 mx-auto" style="max-width: 520px;">
    <div class="card-body p-4">
        <h1 class="h3 mb-2">Kích hoạt tài khoản</h1>
        <p class="text-muted">Nhập mã OTP đã gửi đến email <strong><c:out value="${sessionScope.activationEmail}"/></strong>.</p>
        <c:if test="${not empty alert}">
            <div class="alert alert-error" role="alert"><c:out value="${alert}"/></div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="alert alert-success"><c:out value="${success}"/></div>
        </c:if>
        <form action="<c:url value='/session/register/verify'/>" method="post">
            <div class="mb-3">
                <label class="form-label" for="otp">Mã OTP</label>
                <input class="form-control text-center fs-4" type="text" id="otp" name="otp" required maxlength="6" pattern="[0-9]{6}"
                       value="<c:out value='${otp}'/>" inputmode="numeric" autocomplete="one-time-code"/>
                <div class="form-text">OTP gồm đúng 6 chữ số.</div>
            </div>
            <button class="btn btn-primary w-100" type="submit">Kích hoạt</button>
        </form>
        <form action="<c:url value='/session/register/resend-otp'/>" method="post" class="mt-2">
            <button class="btn btn-outline-secondary w-100" type="submit">Gửi lại OTP</button>
        </form>
    </div>
</section>
