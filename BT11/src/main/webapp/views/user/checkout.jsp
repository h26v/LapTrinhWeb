<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head><title>Thanh toán</title></head>
<body>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<nav class="mb-3"><a href="${ctx}/cart">&laquo; Quay lại giỏ hàng</a></nav>
<h3 class="mb-4"><i class="bi bi-credit-card"></i> Thanh toán</h3>

<c:if test="${not empty error}">
    <div class="alert alert-danger"><c:out value="${error}"/></div>
</c:if>
<c:if test="${hasOverStock}">
    <div class="alert alert-warning">
        Có sách vượt quá số lượng còn trong kho, bạn <a href="${ctx}/cart">quay lại giỏ hàng</a> chỉnh lại trước nhé.
    </div>
</c:if>

<form method="post" action="${ctx}/checkout" id="checkoutForm">
    <div class="row g-4">
        <%-- Thong tin nhan hang + phuong thuc thanh toan --%>
        <div class="col-lg-7">
            <div class="card shadow-sm mb-4">
                <div class="card-header bg-white fw-semibold"><i class="bi bi-geo-alt"></i> Thông tin nhận hàng</div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Họ tên người nhận *</label>
                            <input type="text" name="receiverName" maxlength="50" class="form-control" required
                                   value="<c:out value='${receiverName}'/>">
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Số điện thoại *</label>
                            <input type="tel" name="phone" maxlength="11" pattern="0[0-9]{9,10}" class="form-control" required
                                   placeholder="0901234567" value="<c:out value='${phone}'/>">
                        </div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Địa chỉ nhận hàng *</label>
                        <input type="text" name="address" maxlength="255" class="form-control" required
                               placeholder="Số nhà, đường, phường/xã, quận/huyện, tỉnh/thành"
                               value="<c:out value='${address}'/>">
                    </div>
                    <div>
                        <label class="form-label">Ghi chú</label>
                        <textarea name="note" rows="2" maxlength="255" class="form-control"
                                  placeholder="Ví dụ: giao giờ hành chính"><c:out value="${note}"/></textarea>
                    </div>
                </div>
            </div>

            <div class="card shadow-sm">
                <div class="card-header bg-white fw-semibold"><i class="bi bi-wallet2"></i> Phương thức thanh toán</div>
                <div class="card-body">
                    <div class="form-check border rounded p-3 ps-5">
                        <input class="form-check-input" type="radio" name="paymentMethod" id="pmCod" value="COD" checked>
                        <label class="form-check-label" for="pmCod">
                            <strong>Thanh toán khi nhận hàng (COD)</strong>
                            <div class="small text-secondary">Trả tiền mặt cho shipper khi nhận sách.</div>
                        </label>
                    </div>
                </div>
            </div>
        </div>

        <%-- Tom tat don hang --%>
        <div class="col-lg-5">
            <div class="card shadow-sm">
                <div class="card-header bg-white fw-semibold"><i class="bi bi-bag"></i> Đơn hàng (${items.size()} sách)</div>
                <ul class="list-group list-group-flush">
                    <c:forEach var="item" items="${items}">
                        <li class="list-group-item d-flex align-items-center gap-3">
                            <t:cover src="${item.book.coverImage}" alt="${item.book.title}" cssClass="cart-thumb rounded"/>
                            <div class="flex-grow-1">
                                <div class="fw-semibold"><c:out value="${item.book.title}"/></div>
                                <div class="small text-secondary">${item.quantity} x <t:money value="${item.book.price}"/></div>
                                <c:if test="${item.overStock}">
                                    <div class="small text-danger">Không đủ hàng</div>
                                </c:if>
                            </div>
                            <div class="fw-semibold"><t:money value="${item.subtotal}"/></div>
                        </li>
                    </c:forEach>
                </ul>
                <div class="card-body">
                    <div class="d-flex justify-content-between mb-1">
                        <span>Tạm tính</span><span><t:money value="${total}"/></span>
                    </div>
                    <div class="d-flex justify-content-between mb-2">
                        <span>Phí vận chuyển</span><span class="text-success">Miễn phí</span>
                    </div>
                    <div class="d-flex justify-content-between border-top pt-2 fs-5">
                        <strong>Tổng thanh toán</strong><strong class="text-danger"><t:money value="${total}"/></strong>
                    </div>
                    <button type="submit" class="btn btn-danger w-100 mt-3" ${hasOverStock ? 'disabled' : ''}>
                        <i class="bi bi-bag-check"></i> Đặt hàng
                    </button>
                </div>
            </div>
        </div>
    </div>
</form>

<script>
    // Chong bam dat hang 2 lan
    document.getElementById('checkoutForm').addEventListener('submit', function (e) {
        var btn = this.querySelector('button[type=submit]');
        btn.disabled = true;
        btn.innerHTML = 'Đang đặt hàng...';
    });
</script>
</body>
</html>
