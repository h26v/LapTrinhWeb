<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đơn hàng #${order.orderId}</title>
    <style>
        .order-steps .step { flex: 1; text-align: center; position: relative; }
        /* Duong noi giua cac buoc */
        .order-steps .step::before {
            content: ''; position: absolute; top: 22px; left: -50%; width: 100%; height: 4px; background: #dee2e6;
        }
        .order-steps .step:first-child::before { display: none; }
        .order-steps .step.done::before { background: #198754; }
        .order-steps .step-icon {
            position: relative; z-index: 1; width: 46px; height: 46px; border-radius: 50%;
            display: inline-flex; align-items: center; justify-content: center;
            font-size: 1.3rem; background: #e9ecef; color: #6c757d;
        }
        .order-steps .step.done .step-icon { background: #198754; color: #fff; }
        .order-steps .step.current .step-icon { box-shadow: 0 0 0 5px rgba(25, 135, 84, .25); }
    </style>
</head>
<body>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<nav class="mb-3"><a href="${ctx}/orders">&laquo; Đơn hàng của tôi</a></nav>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
    <h3 class="mb-0">Đơn hàng #${order.orderId}</h3>
    <span class="badge fs-6 text-bg-${order.status.color}"><i class="bi ${order.status.icon}"></i> ${order.status.label}</span>
</div>
<p class="text-secondary">Đặt lúc ${order.createdAtText}</p>

<%-- Tien trinh don hang --%>
<div class="card shadow-sm mb-4">
    <div class="card-body">
        <c:choose>
            <c:when test="${order.status.inFlow}">
                <div class="order-steps d-flex">
                    <c:forEach var="step" items="${flowSteps}">
                        <div class="step ${step.step <= order.status.step ? 'done' : ''} ${step == order.status ? 'current' : ''}">
                            <div class="step-icon"><i class="bi ${step.icon}"></i></div>
                            <div class="small mt-2 ${step == order.status ? 'fw-bold' : 'text-secondary'}">${step.label}</div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:when test="${order.status == 'CANCELLED'}">
                <div class="alert alert-danger mb-0"><i class="bi bi-x-circle"></i> Đơn hàng này đã bị hủy.</div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-dark mb-0"><i class="bi bi-arrow-counterclockwise"></i> Đơn hàng này đã được hoàn trả.</div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<div class="row g-4 mb-4">
    <div class="col-md-6">
        <div class="card shadow-sm h-100">
            <div class="card-header bg-white fw-semibold"><i class="bi bi-geo-alt"></i> Thông tin nhận hàng</div>
            <div class="card-body">
                <p class="mb-1"><strong><c:out value="${order.receiverName}"/></strong> &middot; <c:out value="${order.phone}"/></p>
                <p class="mb-1"><c:out value="${order.address}"/></p>
                <c:if test="${not empty order.note}">
                    <p class="mb-0 text-secondary small">Ghi chú: <c:out value="${order.note}"/></p>
                </c:if>
            </div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="card shadow-sm h-100">
            <div class="card-header bg-white fw-semibold"><i class="bi bi-wallet2"></i> Thanh toán</div>
            <div class="card-body">
                <p class="mb-1">Thanh toán khi nhận hàng (COD)</p>
                <c:choose>
                    <c:when test="${order.status == 'DELIVERED'}">
                        <span class="text-success"><i class="bi bi-check-circle"></i> Đã thanh toán</span>
                    </c:when>
                    <c:when test="${order.status == 'CANCELLED'}">
                        <span class="text-secondary">Không cần thanh toán</span>
                    </c:when>
                    <c:when test="${order.status == 'RETURNED'}">
                        <span class="text-secondary">Đã hoàn hàng</span>
                    </c:when>
                    <c:otherwise>
                        <span class="text-warning-emphasis">
                            Chưa thanh toán, chuẩn bị <strong><t:money value="${order.totalAmount}"/></strong> khi nhận hàng
                        </span>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<div class="table-responsive">
    <table class="table align-middle bg-white shadow-sm">
        <thead class="table-light">
        <tr><th>Sách</th><th class="text-end">Đơn giá</th><th class="text-center">Số lượng</th><th class="text-end">Thành tiền</th></tr>
        </thead>
        <tbody>
        <c:forEach var="i" items="${order.items}">
            <tr>
                <td>
                    <div class="d-flex align-items-center gap-3">
                        <t:cover src="${i.book.coverImage}" alt="${i.bookTitle}" cssClass="cart-thumb rounded"/>
                        <c:choose>
                            <c:when test="${not empty i.book}">
                                <a href="${ctx}/book/detail?id=${i.book.bookid}" class="text-decoration-none">
                                    <c:out value="${i.bookTitle}"/></a>
                            </c:when>
                            <c:otherwise>
                                <span><c:out value="${i.bookTitle}"/> <span class="small text-secondary">(sách đã ngừng bán)</span></span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </td>
                <td class="text-end"><t:money value="${i.price}"/></td>
                <td class="text-center">${i.quantity}</td>
                <td class="text-end"><t:money value="${i.subtotal}"/></td>
            </tr>
        </c:forEach>
        </tbody>
        <tfoot>
        <tr><td colspan="3" class="text-end">Tạm tính</td><td class="text-end"><t:money value="${order.totalAmount}"/></td></tr>
        <tr><td colspan="3" class="text-end">Phí vận chuyển</td><td class="text-end text-success">Miễn phí</td></tr>
        <tr class="fs-5">
            <td colspan="3" class="text-end fw-bold">Tổng cộng</td>
            <td class="text-end fw-bold text-danger"><t:money value="${order.totalAmount}"/></td>
        </tr>
        </tfoot>
    </table>
</div>
</body>
</html>
