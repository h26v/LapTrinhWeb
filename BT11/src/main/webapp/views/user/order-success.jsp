<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head><title>Đặt hàng thành công</title></head>
<body>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="row justify-content-center">
    <div class="col-lg-7">
        <div class="card shadow-sm">
            <div class="card-body p-4 text-center">
                <div class="display-4 text-success"><i class="bi bi-check-circle-fill"></i></div>
                <h3 class="mt-2">Đặt hàng thành công!</h3>
                <p class="text-secondary mb-4">
                    Mã đơn hàng <strong>#${order.orderId}</strong> &middot; ${order.createdAtText}
                </p>

                <div class="alert alert-warning text-start">
                    <i class="bi bi-cash-coin"></i> Thanh toán khi nhận hàng (COD):
                    bạn chuẩn bị <strong><t:money value="${order.totalAmount}"/></strong> để trả cho shipper nhé.
                </div>

                <table class="table table-sm text-start mb-4">
                    <tr><th style="width: 140px">Người nhận</th><td><c:out value="${order.receiverName}"/></td></tr>
                    <tr><th>Số điện thoại</th><td><c:out value="${order.phone}"/></td></tr>
                    <tr><th>Địa chỉ</th><td><c:out value="${order.address}"/></td></tr>
                    <c:if test="${not empty order.note}">
                        <tr><th>Ghi chú</th><td><c:out value="${order.note}"/></td></tr>
                    </c:if>
                    <tr>
                        <th>Trạng thái</th>
                        <td><span class="badge text-bg-${order.status.color}">${order.status.label}</span></td>
                    </tr>
                </table>

                <ul class="list-group text-start mb-4">
                    <c:forEach var="i" items="${order.items}">
                        <li class="list-group-item d-flex justify-content-between">
                            <span><c:out value="${i.bookTitle}"/> <span class="text-secondary">x${i.quantity}</span></span>
                            <span><t:money value="${i.subtotal}"/></span>
                        </li>
                    </c:forEach>
                    <li class="list-group-item d-flex justify-content-between fw-bold">
                        <span>Tổng cộng</span><span class="text-danger"><t:money value="${order.totalAmount}"/></span>
                    </li>
                </ul>

                <a href="${ctx}/orders/detail?id=${order.orderId}" class="btn btn-outline-primary">Xem đơn hàng</a>
                <a href="${ctx}/books" class="btn btn-primary">Tiếp tục mua sách</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>
