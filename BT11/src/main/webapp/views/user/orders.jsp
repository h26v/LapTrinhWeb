<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đơn hàng của tôi</title>
    <style>
        .order-tabs { flex-wrap: nowrap; overflow-x: auto; }
        .order-tabs .nav-link { white-space: nowrap; }
    </style>
</head>
<body>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<h3 class="mb-3"><i class="bi bi-receipt"></i> Đơn hàng của tôi</h3>

<%-- Tab loc theo trang thai --%>
<ul class="nav nav-pills order-tabs bg-white rounded shadow-sm p-2 mb-4">
    <li class="nav-item">
        <a class="nav-link ${empty currentStatus ? 'active' : ''}" href="${ctx}/orders">
            Tất cả <span class="badge rounded-pill text-bg-light">${totalOrders}</span>
        </a>
    </li>
    <c:forEach var="s" items="${statuses}">
        <li class="nav-item">
            <a class="nav-link ${currentStatus == s ? 'active' : ''}" href="${ctx}/orders?status=${s.code}">
                ${s.label} <span class="badge rounded-pill text-bg-light">${counts[s.code]}</span>
            </a>
        </li>
    </c:forEach>
</ul>

<c:if test="${empty result.items}">
    <div class="card card-body text-center py-5">
        <p class="text-secondary mb-3">
            ${empty currentStatus ? 'Bạn chưa có đơn hàng nào.' : 'Không có đơn hàng nào ở trạng thái "'.concat(currentStatus.label).concat('".')}
        </p>
        <div><a href="${ctx}/books" class="btn btn-primary">Đi mua sách</a></div>
    </div>
</c:if>

<c:forEach var="o" items="${result.items}">
    <div class="card shadow-sm mb-3">
        <div class="card-header bg-white d-flex flex-wrap justify-content-between align-items-center gap-2">
            <div>
                <strong>Đơn #${o.orderId}</strong>
                <span class="text-secondary small ms-2"><i class="bi bi-clock"></i> ${o.createdAtText}</span>
            </div>
            <span class="badge text-bg-${o.status.color}"><i class="bi ${o.status.icon}"></i> ${o.status.label}</span>
        </div>
        <ul class="list-group list-group-flush">
            <c:forEach var="i" items="${o.items}">
                <li class="list-group-item d-flex align-items-center gap-3">
                    <t:cover src="${i.book.coverImage}" alt="${i.bookTitle}" cssClass="cart-thumb rounded"/>
                    <div class="flex-grow-1">
                        <div><c:out value="${i.bookTitle}"/></div>
                        <div class="small text-secondary">x${i.quantity}</div>
                    </div>
                    <div><t:money value="${i.subtotal}"/></div>
                </li>
            </c:forEach>
        </ul>
        <div class="card-footer bg-white d-flex flex-wrap justify-content-between align-items-center gap-2">
            <span class="small text-secondary">${o.totalQuantity} cuốn &middot; Thanh toán khi nhận hàng (COD)</span>
            <div>
                Tổng tiền: <strong class="text-danger"><t:money value="${o.totalAmount}"/></strong>
                <a href="${ctx}/orders/detail?id=${o.orderId}" class="btn btn-sm btn-outline-primary ms-2">Xem chi tiết</a>
            </div>
        </div>
    </div>
</c:forEach>

<c:if test="${not empty result.items}">
    <%@ include file="/WEB-INF/includes/pagination.jspf" %>
</c:if>
</body>
</html>
