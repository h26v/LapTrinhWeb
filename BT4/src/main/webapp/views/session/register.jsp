<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<title>Đăng ký tài khoản</title>
<section class="card border-0 mx-auto" style="max-width: 680px;">
    <div class="card-body p-4">
        <h1 class="h3 mb-2">Đăng ký tài khoản</h1>
        <p class="text-muted">Hệ thống sẽ gửi mã OTP đến email để kích hoạt tài khoản.</p>
        <c:if test="${not empty alert}">
            <div class="alert alert-error" role="alert"><c:out value="${alert}"/></div>
        </c:if>
        <form action="<c:url value='/session/register'/>" method="post">
            <div class="mb-3">
                <label class="form-label" for="username">Tên đăng nhập</label>
                <input class="form-control" type="text" id="username" name="username" required maxlength="100"
                       value="<c:out value='${username}'/>" autocomplete="username"/>
                <div class="form-text">Bắt buộc, tối đa 100 ký tự, không chứa khoảng trắng.</div>
            </div>

            <div class="mb-3">
                <label class="form-label" for="email">Email</label>
                <input class="form-control" type="email" id="email" name="email" required maxlength="255"
                       value="<c:out value='${email}'/>" autocomplete="email"/>
            </div>

            <div class="mb-3">
                <label class="form-label" for="fullname">Họ và tên</label>
                <input class="form-control" type="text" id="fullname" name="fullname" required maxlength="255"
                       value="<c:out value='${fullname}'/>"/>
            </div>

            <div class="mb-3">
                <label class="form-label" for="phone">Số điện thoại</label>
                <input class="form-control" type="text" id="phone" name="phone" maxlength="20" inputmode="tel"
                       value="<c:out value='${phone}'/>"/>
            </div>

            <div class="row g-3">
                <div class="col-md-6">
                    <label class="form-label" for="password">Mật khẩu</label>
                    <input class="form-control" type="password" id="password" name="password" required minlength="6"
                           autocomplete="new-password"/>
                </div>
                <div class="col-md-6">
                    <label class="form-label" for="confirmPassword">Xác nhận mật khẩu</label>
                    <input class="form-control" type="password" id="confirmPassword" name="confirmPassword" required minlength="6"
                           autocomplete="new-password"/>
                </div>
            </div>

            <button class="btn btn-primary w-100 mt-4" type="submit">Đăng ký</button>
        </form>
        <p class="mt-3 mb-0">Đã có tài khoản? <a href="<c:url value='/session/login'/>">Đăng nhập</a></p>
    </div>
</section>
