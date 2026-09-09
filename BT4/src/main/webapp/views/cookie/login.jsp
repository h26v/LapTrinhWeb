<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<title>Đăng nhập Cookie</title>
<section class="card border-0 mx-auto" style="max-width: 520px;">
    <div class="card-body p-4">
        <h1 class="h3 mb-3">Đăng nhập Cookie</h1>
        <c:if test="${not empty alert}">
            <div class="alert alert-error" role="alert"><c:out value="${alert}"/></div>
        </c:if>
        <form action="<c:url value='/cookie/login'/>" method="post">
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
        <p class="text-muted mt-3 mb-0">Tài khoản demo: <code>trung</code> / <code>123</code></p>
    </div>
</section>
