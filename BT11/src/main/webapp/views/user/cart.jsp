<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head><title>Giỏ hàng</title></head>
<body>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<h3 class="mb-4"><i class="bi bi-cart3"></i> Giỏ hàng của bạn</h3>

<c:choose>
    <c:when test="${empty items}">
        <div class="card card-body text-center py-5">
            <p class="text-secondary mb-3">Giỏ hàng đang trống.</p>
            <div><a href="${ctx}/books" class="btn btn-primary">Đi chọn sách</a></div>
        </div>
    </c:when>
    <c:otherwise>
        <c:if test="${hasOverStock}">
            <div class="alert alert-warning">
                Có sách trong giỏ vượt quá số lượng còn trong kho, bạn giảm số lượng hoặc xóa sách đó trước khi thanh toán nhé.
            </div>
        </c:if>

        <div class="table-responsive">
            <table class="table align-middle bg-white">
                <thead class="table-light">
                <tr>
                    <th>Sách</th>
                    <th class="text-end">Đơn giá</th>
                    <th class="text-center">Số lượng</th>
                    <th class="text-end">Thành tiền</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="item" items="${items}">
                    <tr class="${item.overStock ? 'table-warning' : ''}">
                        <td>
                            <div class="d-flex align-items-center gap-3">
                                <t:cover src="${item.book.coverImage}" alt="${item.book.title}" cssClass="cart-thumb rounded"/>
                                <div>
                                    <a href="${ctx}/book/detail?id=${item.book.bookid}" class="fw-semibold text-decoration-none">
                                        <c:out value="${item.book.title}"/></a>
                                    <div class="small text-secondary">Kho còn: ${item.book.quantity}</div>
                                    <c:if test="${item.overStock}">
                                        <div class="small text-danger">
                                            ${item.maxQuantity == 0 ? 'Sách đã hết hàng' : 'Chỉ mua được tối đa '.concat(item.maxQuantity).concat(' cuốn')}
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </td>
                        <td class="text-end"><t:money value="${item.book.price}"/></td>
                        <td class="text-center">
                            <form method="post" action="${ctx}/cart/update" class="qty-form d-inline-flex align-items-center">
                                <input type="hidden" name="itemId" value="${item.id}">
                                <button type="button" class="btn btn-outline-secondary btn-sm" data-step="-1" title="Giảm"
                                    ${item.quantity <= 1 ? 'disabled' : ''}>&minus;</button>
                                <input type="number" name="quantity" value="${item.quantity}" min="1" max="${item.maxQuantity}"
                                       class="form-control form-control-sm text-center mx-1" style="width: 64px">
                                <button type="button" class="btn btn-outline-secondary btn-sm" data-step="1" title="Tăng"
                                    ${item.quantity >= item.maxQuantity ? 'disabled' : ''}>+</button>
                            </form>
                            <div class="small text-secondary mt-1">Tối đa ${item.maxQuantity}</div>
                        </td>
                        <td class="text-end fw-semibold"><t:money value="${item.subtotal}"/></td>
                        <td class="text-end">
                            <form method="post" action="${ctx}/cart/remove" onsubmit="return confirm('Xóa sách này khỏi giỏ?')">
                                <input type="hidden" name="itemId" value="${item.id}">
                                <button class="btn btn-sm btn-outline-danger" title="Xóa"><i class="bi bi-trash"></i></button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="d-flex flex-wrap justify-content-between align-items-center gap-3">
            <div class="d-flex gap-2">
                <a href="${ctx}/books" class="btn btn-outline-primary">&laquo; Mua thêm</a>
                <form method="post" action="${ctx}/cart/clear" onsubmit="return confirm('Xóa hết giỏ hàng?')">
                    <button class="btn btn-outline-danger"><i class="bi bi-x-circle"></i> Xóa hết</button>
                </form>
            </div>
            <div class="text-end">
                <div class="fs-5">Tổng cộng: <strong class="text-danger"><t:money value="${total}"/></strong></div>
                <c:choose>
                    <c:when test="${hasOverStock}">
                        <button class="btn btn-danger mt-2" disabled>Thanh toán</button>
                    </c:when>
                    <c:otherwise>
                        <a href="${ctx}/checkout" class="btn btn-danger mt-2"><i class="bi bi-credit-card"></i> Thanh toán</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <script>
            // Nut +/- va o nhap so luong: doi xong la gui form luon
            document.querySelectorAll('.qty-form').forEach(function (form) {
                var input = form.querySelector('input[name=quantity]');
                form.querySelectorAll('[data-step]').forEach(function (btn) {
                    btn.addEventListener('click', function () {
                        var value = (parseInt(input.value, 10) || 1) + parseInt(btn.dataset.step, 10);
                        if (value < 1 || value > parseInt(input.max, 10)) {
                            return;
                        }
                        input.value = value;
                        form.submit();
                    });
                });
                input.addEventListener('change', function () {
                    form.submit();
                });
            });
        </script>
    </c:otherwise>
</c:choose>
</body>
</html>
