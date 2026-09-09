<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<title>Đặt lại mật khẩu</title>
<section class="card border-0 mx-auto" style="max-width: 520px;">
    <div class="card-body p-4">
        <h1 class="h3 mb-2">Đặt lại mật khẩu</h1>
        <p class="text-muted">Nhập OTP đã gửi đến email <strong><c:out value="${sessionScope.resetEmail}"/></strong> và mật khẩu mới.</p>
        <c:if test="${not empty alert}">
            <div class="alert alert-error" role="alert"><c:out value="${alert}"/></div>
        </c:if>
        <form action="<c:url value='/session/reset-password'/>" method="post">
            <div class="mb-3">
                <label class="form-label" for="otp">Mã OTP</label>
                <input class="form-control text-center fs-4" type="text" id="otp" name="otp" required maxlength="6" pattern="[0-9]{6}"
                       value="<c:out value='${otp}'/>" inputmode="numeric" autocomplete="one-time-code"/>
            </div>

            <div class="mb-3">
                <label class="form-label" for="password">Mật khẩu mới</label>
                <input class="form-control" type="password" id="password" name="password" required minlength="6"
                       autocomplete="new-password"/>
            </div>

            <div class="mb-3">
                <label class="form-label" for="confirmPassword">Xác nhận mật khẩu mới</label>
                <input class="form-control" type="password" id="confirmPassword" name="confirmPassword" required minlength="6"
                       autocomplete="new-password"/>
            </div>

            <button class="btn btn-primary w-100" type="submit">Đặt lại mật khẩu</button>
        </form>
    </div>
</section>
