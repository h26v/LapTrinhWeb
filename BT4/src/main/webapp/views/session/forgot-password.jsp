<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<title>Quên mật khẩu</title>
<section class="card border-0 mx-auto" style="max-width: 520px;">
    <div class="card-body p-4">
        <h1 class="h3 mb-2">Quên mật khẩu</h1>
        <p class="text-muted">Nhập email tài khoản để nhận mã OTP đặt lại mật khẩu.</p>
        <c:if test="${not empty alert}">
            <div class="alert alert-error" role="alert"><c:out value="${alert}"/></div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="alert alert-success"><c:out value="${success}"/></div>
        </c:if>
        <form action="<c:url value='/session/forgot-password'/>" method="post">
            <div class="mb-3">
                <label class="form-label" for="email">Email</label>
                <input class="form-control" type="email" id="email" name="email" required maxlength="255"
                       value="<c:out value='${email}'/>" autocomplete="email"/>
            </div>
            <button class="btn btn-primary w-100" type="submit">Gửi OTP</button>
        </form>
        <p class="mt-3 mb-0"><a href="<c:url value='/session/login'/>">Quay lại đăng nhập</a></p>
    </div>
</section>
