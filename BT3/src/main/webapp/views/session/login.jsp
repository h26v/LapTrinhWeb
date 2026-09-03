<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<title>Đăng nhập Session</title>
<section class="card" style="max-width: 520px; margin: 0 auto;">
    <h1>Đăng nhập Session</h1>
    <c:if test="${not empty alert}">
        <p class="alert alert-error" role="alert"><c:out value="${alert}"/></p>
    </c:if>
    <form action="<c:url value='/session/login'/>" method="post">
        <label for="username">Tên đăng nhập</label>
        <input type="text" id="username" name="username" required autocomplete="username"
               style="width:100%;max-width:460px;padding:10px;border:1px solid #c8d3e2;border-radius:6px;"/>
        <label for="password">Mật khẩu</label>
        <input type="password" id="password" name="password" required autocomplete="current-password"
               style="width:100%;max-width:460px;padding:10px;border:1px solid #c8d3e2;border-radius:6px;"/>
        <div><button type="submit">Đăng nhập</button></div>
    </form>
    <p class="account-meta">Tài khoản demo: <code>trung</code> / <code>123</code></p>
</section>
