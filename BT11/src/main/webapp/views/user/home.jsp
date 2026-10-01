<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head><title>${isProductPage ? 'Sản phẩm' : 'Trang chủ'}</title></head>
<body>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%-- Trang hien tai: them vao gio xong quay lai day --%>
<c:set var="backUrl" value="${isProductPage ? '/books' : '/home'}?page=${result.page}"/>
<c:url var="loginUrl" value="/login"><c:param name="next" value="${backUrl}"/></c:url>

<h3 class="mb-4">
    <c:choose>
        <c:when test="${isProductPage}">Tất cả sản phẩm</c:when>
        <c:when test="${not empty sessionScope.account}">
            Xin chào, <c:out value="${sessionScope.account.fullname}"/> — Sách mới nhất
        </c:when>
        <c:otherwise>Sách mới nhất</c:otherwise>
    </c:choose>
</h3>

<c:if test="${empty result.items}">
    <div class="alert alert-warning">Chưa có sách nào.</div>
</c:if>

<div class="row row-cols-1 row-cols-sm-2 row-cols-lg-3 g-4">
    <c:forEach var="b" items="${result.items}">
        <div class="col">
            <div class="card h-100 shadow-sm">
                <%-- [cover_image] --%>
                <a href="${ctx}/book/detail?id=${b.bookid}">
                    <t:cover src="${b.coverImage}" alt="${b.title}" cssClass="card-img-top book-cover"/>
                </a>
                <div class="card-body">
                    <p class="mb-1"><strong>Tiêu đề:</strong>
                        <a href="${ctx}/book/detail?id=${b.bookid}"><c:out value="${b.title}"/></a></p>
                    <p class="mb-1"><strong>Mã isbn:</strong> ${b.isbn}</p>
                    <p class="mb-1"><strong>Tác giả:</strong> <c:out value="${b.authorNames}"/></p>
                    <p class="mb-1"><strong>Publisher:</strong> <c:out value="${b.publisher}"/></p>
                    <p class="mb-1"><strong>Publisher_date:</strong> ${b.publishDate}</p>
                    <p class="mb-1"><strong>Quantity:</strong> ${b.quantity}</p>
                    <p class="mb-1"><strong>Price:</strong> <t:money value="${b.price}"/></p>
                    <p class="mb-0">
                        <a href="${ctx}/book/detail?id=${b.bookid}#reviews">Review (${b.reviewCount})</a>
                    </p>
                </div>
                <div class="card-footer bg-white border-0 pt-0 pb-3">
                    <c:choose>
                        <c:when test="${not b.inStock}">
                            <button class="btn btn-secondary btn-sm w-100" disabled>Hết hàng</button>
                        </c:when>
                        <c:when test="${empty sessionScope.account}">
                            <a href="${loginUrl}" class="btn btn-outline-success btn-sm w-100">
                                <i class="bi bi-cart-plus"></i> Đăng nhập để mua</a>
                        </c:when>
                        <c:otherwise>
                            <form method="post" action="${ctx}/cart/add">
                                <input type="hidden" name="bookid" value="${b.bookid}">
                                <input type="hidden" name="quantity" value="1">
                                <input type="hidden" name="back" value="${backUrl}">
                                <button class="btn btn-success btn-sm w-100"><i class="bi bi-cart-plus"></i> Thêm vào giỏ</button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </c:forEach>
</div>

<div class="mt-4">
    <%@ include file="/WEB-INF/includes/pagination.jspf" %>
</div>
</body>
</html>
