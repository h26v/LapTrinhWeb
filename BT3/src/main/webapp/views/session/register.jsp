<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<title>Đăng ký tài khoản</title>
<section class="card" style="max-width: 620px; margin: 0 auto;">
    <h1>Đăng ký tài khoản</h1>
    <p>Hệ thống sẽ gửi mã OTP đến email để kích hoạt tài khoản.</p>
    <c:if test="${not empty alert}">
        <p class="alert alert-error" role="alert"><c:out value="${alert}"/></p>
    </c:if>
    <form action="<c:url value='/session/register'/>" method="post">
        <label for="username">Tên đăng nhập</label>
        <input type="text" id="username" name="username" required maxlength="100"
               value="<c:out value='${username}'/>" autocomplete="username"
               style="width:100%;padding:10px;border:1px solid #c8d3e2;border-radius:6px;"/>

        <label for="email">Email</label>
        <input type="email" id="email" name="email" required maxlength="255"
               value="<c:out value='${email}'/>" autocomplete="email"
               style="width:100%;padding:10px;border:1px solid #c8d3e2;border-radius:6px;"/>

        <label for="fullname">Họ và tên</label>
        <input type="text" id="fullname" name="fullname" maxlength="255"
               value="<c:out value='${fullname}'/>"
               style="width:100%;padding:10px;border:1px solid #c8d3e2;border-radius:6px;"/>

        <label for="phone">Số điện thoại</label>
        <input type="text" id="phone" name="phone" maxlength="20"
               value="<c:out value='${phone}'/>"
               style="width:100%;padding:10px;border:1px solid #c8d3e2;border-radius:6px;"/>

        <label for="password">Mật khẩu</label>
        <input type="password" id="password" name="password" required minlength="6"
               autocomplete="new-password"
               style="width:100%;padding:10px;border:1px solid #c8d3e2;border-radius:6px;"/>

        <label for="confirmPassword">Xác nhận mật khẩu</label>
        <input type="password" id="confirmPassword" name="confirmPassword" required minlength="6"
               autocomplete="new-password"
               style="width:100%;padding:10px;border:1px solid #c8d3e2;border-radius:6px;"/>

        <div style="margin-top:16px;"><button type="submit">Đăng ký</button></div>
    </form>
    <p>Đã có tài khoản? <a href="<c:url value='/session/login'/>">Đăng nhập</a></p>
</section>
