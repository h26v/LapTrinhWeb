<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Thêm người dùng" scope="request"/>
<c:set var="adminTab" value="user" scope="request"/>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>

<section class="card border-0 mx-auto" style="max-width: 720px;">
    <div class="card-body p-4">
        <div class="d-flex justify-content-between align-items-center gap-3 flex-wrap mb-3">
            <h1 class="h3 mb-0">Thêm người dùng</h1>
            <a class="btn btn-outline-secondary" href="<c:url value='/admin/users'/>">Quay lại</a>
        </div>

        <jsp:include page="/WEB-INF/views/layout/messages.jsp"/>

        <form action="<c:url value='/admin/user/insert'/>" method="post" enctype="multipart/form-data">
            <div class="row g-3">
                <div class="col-md-6">
                    <label class="form-label" for="username">Tên đăng nhập <span class="text-danger">*</span></label>
                    <input class="form-control" type="text" id="username" name="username" required maxlength="100"
                           value="<c:out value='${user.userName}'/>"/>
                </div>
                <div class="col-md-6">
                    <label class="form-label" for="password">Mật khẩu <span class="text-danger">*</span></label>
                    <input class="form-control" type="password" id="password" name="password" required minlength="6"
                           autocomplete="new-password"/>
                    <div class="form-text">Tối thiểu 6 ký tự.</div>
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
                    <label class="form-label" for="avatar">Ảnh đại diện</label>
                    <input class="form-control" type="file" id="avatar" name="avatar"
                           accept="image/png,image/jpeg,image/gif,image/bmp"/>
                    <div class="form-text">PNG, JPEG, GIF hoặc BMP; tối đa 5 MB.</div>
                </div>

                <div class="col-md-6">
                    <label class="form-label d-block">Vai trò</label>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" id="roleUser" name="roleid" value="2"
                               ${empty user || user.roleid != 1 ? 'checked' : ''}/>
                        <label class="form-check-label" for="roleUser">Người dùng</label>
                    </div>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" id="roleAdmin" name="roleid" value="1"
                               ${not empty user && user.roleid == 1 ? 'checked' : ''}/>
                        <label class="form-check-label" for="roleAdmin">Admin</label>
                    </div>
                </div>
                <div class="col-md-6">
                    <label class="form-label d-block">Trạng thái</label>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" id="active1" name="active" value="1"
                               ${empty user || user.active == 1 ? 'checked' : ''}/>
                        <label class="form-check-label" for="active1">Hoạt động</label>
                    </div>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" id="active0" name="active" value="0"
                               ${not empty user && user.active == 0 ? 'checked' : ''}/>
                        <label class="form-check-label" for="active0">Khóa</label>
                    </div>
                </div>
            </div>

            <button class="btn btn-primary mt-4" type="submit">Lưu người dùng</button>
        </form>
    </div>
</section>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
