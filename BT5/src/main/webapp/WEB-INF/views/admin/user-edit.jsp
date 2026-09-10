<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="Sửa người dùng" scope="request"/>
<c:set var="adminTab" value="user" scope="request"/>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>

<section class="card border-0 mx-auto" style="max-width: 720px;">
    <div class="card-body p-4">
        <div class="d-flex justify-content-between align-items-center gap-3 flex-wrap mb-3">
            <h1 class="h3 mb-0">Sửa người dùng #<c:out value="${user.id}"/></h1>
            <a class="btn btn-outline-secondary" href="<c:url value='/admin/users'/>">Quay lại</a>
        </div>

        <jsp:include page="/WEB-INF/views/layout/messages.jsp"/>

        <form action="<c:url value='/admin/user/update'/>" method="post" enctype="multipart/form-data">
            <input type="hidden" name="id" value="<c:out value='${user.id}'/>"/>

            <div class="row g-3">
                <div class="col-md-6">
                    <label class="form-label" for="username">Tên đăng nhập <span class="text-danger">*</span></label>
                    <input class="form-control" type="text" id="username" name="username" required maxlength="100"
                           value="<c:out value='${user.userName}'/>"/>
                </div>
                <div class="col-md-6">
                    <label class="form-label" for="password">Mật khẩu mới</label>
                    <input class="form-control" type="password" id="password" name="password" minlength="6"
                           autocomplete="new-password"/>
                    <div class="form-text">Bỏ trống để giữ mật khẩu hiện tại.</div>
                </div>

                <div class="col-md-6">
                    <label class="form-label" for="fullname">Họ và tên <span class="text-danger">*</span></label>
                    <input class="form-control" type="text" id="fullname" name="fullname" required maxlength="255"
                           value="<c:out value='${user.fullName}'/>"/>
                </div>
                <div class="col-md-6">
                    <label class="form-label" for="email">Email</label>
                    <input class="form-control" type="email" id="email" name="email" maxlength="255"
                           value="<c:out value='${user.email}'/>"/>
                </div>

                <div class="col-md-6">
                    <label class="form-label" for="phone">Điện thoại</label>
                    <input class="form-control" type="text" id="phone" name="phone" maxlength="20"
                           value="<c:out value='${user.phone}'/>"/>
                </div>
                <div class="col-md-6">
                    <label class="form-label d-block">Ảnh hiện tại</label>
                    <c:choose>
                        <c:when test="${empty user.avatar}">
                            <span class="text-muted">Chưa có ảnh.</span>
                        </c:when>
                        <c:when test="${fn:startsWith(user.avatar, 'https://') || fn:startsWith(user.avatar, 'http://')}">
                            <img class="avatar-sm" src="<c:out value='${user.avatar}'/>" alt="Ảnh đại diện"/>
                        </c:when>
                        <c:otherwise>
                            <c:url var="avatarUrl" value="/image">
                                <c:param name="fname" value="${user.avatar}"/>
                            </c:url>
                            <img class="avatar-sm" src="${avatarUrl}" alt="Ảnh đại diện"/>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="col-md-6">
                    <label class="form-label" for="avatar">Đổi ảnh khác</label>
                    <input class="form-control" type="file" id="avatar" name="avatar"
                           accept="image/png,image/jpeg,image/gif,image/bmp"/>
                    <div class="form-text">Bỏ trống để giữ ảnh hiện tại.</div>
                </div>

                <div class="col-md-6">
                    <label class="form-label d-block">Vai trò</label>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" id="roleUser" name="roleid" value="2"
                               ${user.roleid != 1 ? 'checked' : ''}/>
                        <label class="form-check-label" for="roleUser">Người dùng</label>
                    </div>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" id="roleAdmin" name="roleid" value="1"
                               ${user.roleid == 1 ? 'checked' : ''}/>
                        <label class="form-check-label" for="roleAdmin">Admin</label>
                    </div>
                </div>
                <div class="col-md-6">
                    <label class="form-label d-block">Trạng thái</label>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" id="active1" name="active" value="1"
                               ${user.active == 1 ? 'checked' : ''}/>
                        <label class="form-check-label" for="active1">Hoạt động</label>
                    </div>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" id="active0" name="active" value="0"
                               ${user.active == 0 ? 'checked' : ''}/>
                        <label class="form-check-label" for="active0">Khóa</label>
                    </div>
                </div>
            </div>

            <button class="btn btn-primary mt-4" type="submit">Cập nhật</button>
        </form>
    </div>
</section>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
