<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Đăng nhập</title></head>
<body>
<div class="row justify-content-center">
    <div class="col-md-5">
        <div class="card shadow-sm">
            <div class="card-body p-4">
                <h3 class="mb-3 text-center">Đăng nhập</h3>

                <c:if test="${param.registered == '1'}">
                    <div class="alert alert-success">Kích hoạt tài khoản thành công, mời đăng nhập.</div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger"><c:out value="${error}"/></div>
                </c:if>

                <c:if test="${not empty param.next}">
                    <div class="alert alert-info">Bạn cần đăng nhập để tiếp tục.</div>
                </c:if>

                <form method="post" action="${pageContext.request.contextPath}/login">
                    <input type="hidden" name="next" value="<c:out value='${param.next}'/>">
                    <div class="mb-3">
                        <label class="form-label">Email</label>
                        <input type="email" name="email" class="form-control" required value="<c:out value='${email}'/>">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Mật khẩu</label>
                        <input type="password" name="password" class="form-control" required>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Đăng nhập</button>
                </form>
                <p class="mt-3 text-center">Chưa có tài khoản?
                    <a href="${pageContext.request.contextPath}/register">Đăng ký</a></p>
            </div>
        </div>
    </div>
</div>
</body>
</html>
