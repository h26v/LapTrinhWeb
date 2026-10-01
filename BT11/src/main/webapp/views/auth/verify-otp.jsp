<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Xác thực OTP</title></head>
<body>
<div class="row justify-content-center">
    <div class="col-md-5">
        <div class="card shadow-sm">
            <div class="card-body p-4">
                <h3 class="mb-3 text-center">Kích hoạt tài khoản</h3>
                <p class="text-center">Nhập mã OTP đã gửi tới <strong><c:out value="${pendingEmail}"/></strong></p>

                <c:if test="${not empty sessionScope.otpMessage}">
                    <div class="alert alert-info"><c:out value="${sessionScope.otpMessage}"/></div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger"><c:out value="${error}"/></div>
                </c:if>

                <form method="post" action="${pageContext.request.contextPath}/verify-otp">
                    <div class="mb-3">
                        <input type="text" name="otp" maxlength="6" pattern="[0-9]{6}" required autofocus
                               class="form-control form-control-lg text-center" placeholder="______">
                    </div>
                    <button type="submit" class="btn btn-success w-100">Xác nhận</button>
                </form>
                <form method="post" action="${pageContext.request.contextPath}/verify-otp" class="mt-2">
                    <input type="hidden" name="action" value="resend">
                    <button type="submit" class="btn btn-link w-100">Gửi lại mã</button>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
