<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<title>Trang chủ BT4</title>
<section class="card border-0">
    <div class="card-body p-4">
        <div class="d-flex justify-content-between align-items-center gap-3 flex-wrap mb-3">
            <div>
                <h1 class="h3 mb-1">10 sản phẩm mới nhất</h1>
                <p class="text-muted mb-0">Danh sách sản phẩm đang hiển thị theo thời gian tạo mới nhất.</p>
            </div>
            <a class="btn btn-outline-primary" href="<c:url value='/product'/>">Xem tất cả sản phẩm</a>
        </div>
        <div class="product-grid">
            <c:forEach var="product" items="${products}">
                <c:url var="detailUrl" value="/product/detail">
                    <c:param name="id" value="${product.productId}"/>
                </c:url>
                <article class="card product-card border-0">
                    <a href="${detailUrl}">
                        <c:choose>
                            <c:when test="${not empty product.images && (fn:startsWith(product.images, 'https://') || fn:startsWith(product.images, 'http://'))}">
                                <img src="<c:out value='${product.images}'/>" alt="${product.productName}" loading="lazy"/>
                            </c:when>
                            <c:when test="${not empty product.images}">
                                <c:url var="imageUrl" value="/image">
                                    <c:param name="fname" value="${product.images}"/>
                                </c:url>
                                <img src="${imageUrl}" alt="${product.productName}" loading="lazy"/>
                            </c:when>
                            <c:otherwise><div class="product-placeholder">No image</div></c:otherwise>
                        </c:choose>
                        <h2><c:out value="${product.productName}"/></h2>
                    </a>
                    <p class="price fs-6"><fmt:formatNumber value="${product.price}" type="number"/> đ</p>
                    <p class="text-muted"><c:out value="${product.category.categoryname}"/></p>
                </article>
            </c:forEach>
            <c:if test="${empty products}">
                <div class="alert alert-info mb-0">Chưa có sản phẩm hiển thị. Vui lòng thêm sản phẩm trong trang quản trị.</div>
            </c:if>
        </div>
    </div>
</section>
