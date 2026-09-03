<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Profile người dùng</title>
<style>
    .profile-grid { display: grid; grid-template-columns: 220px 1fr; gap: 28px; align-items: start; }
    .avatar { width: 180px; height: 180px; border-radius: 50%; object-fit: cover; border: 1px solid #dce4ef; background: #eef3f9; }
    .avatar-empty { width: 180px; height: 180px; border-radius: 50%; display: grid; place-items: center;
                    background: #eef3f9; color: #69758a; text-align: center; }
    label { display: block; margin: 16px 0 6px; font-weight: 700; }
    input[type=text], input[type=file] { width: 100%; max-width: 460px; padding: 10px; border: 1px solid #c8d3e2; border-radius: 6px; }
    button { margin-top: 22px; padding: 10px 18px; border: 0; border-radius: 6px; background: #2563eb; color: white; cursor: pointer; }
    button:hover { background: #1d4ed8; }
    .alert { border-radius: 6px; padding: 10px 12px; margin-bottom: 18px; }
    .alert-error { color: #991b1b; background: #fef2f2; border: 1px solid #fecaca; }
    .alert-success { color: #166534; background: #f0fdf4; border: 1px solid #bbf7d0; }
    .account-meta { color: #69758a; margin-top: 6px; }
    @media (max-width: 640px) { .profile-grid { grid-template-columns: 1fr; } }
</style>
</head>
<body>
<section class="card">
    <h1>Cập nhật profile</h1>
    <p class="account-meta">Tài khoản: <strong><c:out value="${account.userName}"/></strong>
        · Email: <c:out value="${account.email}"/></p>

    <c:if test="${not empty requestScope.profileError}">
        <div class="alert alert-error" role="alert"><c:out value="${requestScope.profileError}"/></div>
    </c:if>
    <c:if test="${not empty sessionScope.profileSuccess}">
        <div class="alert alert-success" role="status"><c:out value="${sessionScope.profileSuccess}"/></div>
        <c:remove var="profileSuccess" scope="session"/>
    </c:if>

    <div class="profile-grid">
        <div>
            <c:choose>
                <c:when test="${empty account.avatar}">
                    <div class="avatar-empty">Chưa có ảnh đại diện</div>
                </c:when>
                <c:when test="${fn:startsWith(account.avatar, 'https://')}">
                    <img class="avatar" src="<c:out value='${account.avatar}'/>" alt="Ảnh đại diện"/>
                </c:when>
                <c:otherwise>
                    <img class="avatar" src="<c:url value='/image'><c:param name='fname' value='${account.avatar}'/></c:url>"
                         alt="Ảnh đại diện"/>
                </c:otherwise>
            </c:choose>
        </div>

        <form action="<c:url value='/session/profile/update'/>" method="post" enctype="multipart/form-data">
            <label for="fullName">Họ và tên</label>
            <input type="text" id="fullName" name="fullname" maxlength="255" required
                   value="<c:out value='${formFullName != null ? formFullName : account.fullName}'/>"/>

            <label for="phone">Số điện thoại</label>
            <input type="text" id="phone" name="phone" maxlength="20" inputmode="tel"
                   value="<c:out value='${formPhone != null ? formPhone : account.phone}'/>"/>

            <label for="images">Ảnh đại diện mới</label>
            <input type="file" id="images" name="images" accept="image/png,image/jpeg,image/gif,image/bmp"/>
            <small>PNG, JPEG, GIF hoặc BMP; tối đa 5 MB.</small>

            <div><button type="submit">Lưu thay đổi</button></div>
        </form>
    </div>
</section>
</body>
</html>
