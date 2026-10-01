<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Đăng ký</title></head>
<body>
<div class="row justify-content-center">
    <div class="col-md-6">
        <div class="card shadow-sm">
            <div class="card-body p-4">
                <h3 class="mb-3 text-center">Đăng ký tài khoản</h3>
                <p class="text-secondary text-center small">Tài khoản được kích hoạt bằng mã OTP gửi qua email.</p>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger"><c:out value="${error}"/></div>
                </c:if>

                <form method="post" action="${pageContext.request.contextPath}/register">
                    <div class="mb-3">
                        <label class="form-label">Email *</label>
                        <input type="email" name="email" maxlength="50" class="form-control" required
                               value="<c:out value='${email}'/>">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Họ tên *</label>
                        <input type="text" name="fullname" maxlength="50" class="form-control" required
                               value="<c:out value='${fullname}'/>">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Số điện thoại</label>
                        <input type="text" name="phone" inputmode="numeric" pattern="[0-9]{1,10}" class="form-control"
                               value="<c:out value='${phone}'/>">
                    </div>
                    <div class="row">
                        <div class="col mb-3">
                            <label class="form-label">Mật khẩu *</label>
                            <input type="password" name="password" minlength="6" class="form-control" required>
                        </div>
                        <div class="col mb-3">
                            <label class="form-label">Nhập lại mật khẩu *</label>
                            <input type="password" name="confirm" minlength="6" class="form-control" required>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Đăng ký &amp; gửi OTP</button>
                </form>
                <p class="mt-3 text-center">Đã có tài khoản?
                    <a href="${pageContext.request.contextPath}/login">Đăng nhập</a></p>
            </div>
        </div>
    </div>
</div>
</body>
</html>
