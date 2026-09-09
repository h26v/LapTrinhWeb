<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<title>Đăng nhập Session</title>
<section class="card border-0 mx-auto" style="max-width: 520px;">
    <div class="card-body p-4">
        <h1 class="h3 mb-3">Đăng nhập Session</h1>
        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success"><c:out value="${sessionScope.success}"/></div>
            <c:remove var="success" scope="session"/>
        </c:if>
        <c:if test="${not empty alert}">
            <div class="alert alert-error" role="alert"><c:out value="${alert}"/></div>
        </c:if>
        <form action="<c:url value='/session/login'/>" method="post">
            <div class="mb-3">
                <label class="form-label" for="username">Tên đăng nhập</label>
                <input class="form-control" type="text" id="username" name="username" required maxlength="100"
                       value="<c:out value='${username}'/>" autocomplete="username"/>
            </div>
            <div class="mb-3">
                <label class="form-label" for="password">Mật khẩu</label>
                <input class="form-control" type="password" id="password" name="password" required
                       autocomplete="current-password"/>
            </div>
            <button class="btn btn-primary w-100" type="submit">Đăng nhập</button>
        </form>
        <div class="d-flex justify-content-between gap-2 mt-3 flex-wrap">
            <a href="<c:url value='/session/register'/>">Đăng ký tài khoản</a>
            <a href="<c:url value='/session/forgot-password'/>">Quên mật khẩu?</a>
        </div>
        <p class="text-muted mt-3 mb-0">Tài khoản demo: <code>trung</code> / <code>123</code></p>
    </div>
</section>
