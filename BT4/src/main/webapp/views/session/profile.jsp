<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<title>Profile người dùng</title>
<section class="card border-0">
    <div class="card-body p-4">
        <div class="d-flex justify-content-between align-items-start gap-3 flex-wrap mb-4">
            <div>
                <h1 class="h3 mb-1">Cập nhật profile</h1>
                <p class="text-muted mb-0">Tài khoản: <strong><c:out value="${account.userName}"/></strong>
                    · Email: <c:out value="${account.email}"/></p>
            </div>
            <a class="btn btn-outline-secondary" href="<c:url value='/home'/>">Về trang chủ</a>
        </div>

        <c:if test="${not empty requestScope.profileError}">
            <div class="alert alert-error" role="alert"><c:out value="${requestScope.profileError}"/></div>
        </c:if>
        <c:if test="${not empty sessionScope.profileSuccess}">
            <div class="alert alert-success" role="status"><c:out value="${sessionScope.profileSuccess}"/></div>
            <c:remove var="profileSuccess" scope="session"/>
        </c:if>

        <div class="row g-4 align-items-start">
            <div class="col-md-4 text-center text-md-start">
                <c:choose>
                    <c:when test="${empty account.avatar}">
                        <div class="avatar-empty mx-auto mx-md-0">Chưa có ảnh đại diện</div>
                    </c:when>
                    <c:when test="${fn:startsWith(account.avatar, 'https://') || fn:startsWith(account.avatar, 'http://')}">
                        <img class="avatar mx-auto mx-md-0 d-block" src="<c:out value='${account.avatar}'/>" alt="Ảnh đại diện"/>
                    </c:when>
                    <c:otherwise>
                        <c:url var="avatarUrl" value="/image">
                            <c:param name="fname" value="${account.avatar}"/>
                        </c:url>
                        <img class="avatar mx-auto mx-md-0 d-block" src="${avatarUrl}" alt="Ảnh đại diện"/>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="col-md-8">
                <form action="<c:url value='/session/profile/update'/>" method="post" enctype="multipart/form-data">
                    <div class="mb-3">
                        <label class="form-label" for="fullName">Họ và tên</label>
                        <input class="form-control" type="text" id="fullName" name="fullname" maxlength="255" required
                               value="<c:out value='${formFullName != null ? formFullName : account.fullName}'/>"/>
                        <div class="form-text">Bắt buộc, tối đa 255 ký tự.</div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label" for="phone">Số điện thoại</label>
                        <input class="form-control" type="text" id="phone" name="phone" maxlength="20" inputmode="tel"
                               value="<c:out value='${formPhone != null ? formPhone : account.phone}'/>"/>
                        <div class="form-text">Có thể bỏ trống; chỉ nhận số và các ký tự + ( ) . -</div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label" for="images">Ảnh đại diện mới</label>
                        <input class="form-control" type="file" id="images" name="images"
                               accept="image/png,image/jpeg,image/gif,image/bmp"/>
                        <div class="form-text">PNG, JPEG, GIF hoặc BMP; tối đa 5 MB.</div>
                    </div>

                    <button class="btn btn-primary" type="submit">Lưu thay đổi</button>
                </form>
            </div>
        </div>
    </div>
</section>
