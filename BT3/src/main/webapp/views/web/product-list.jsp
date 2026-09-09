<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<title>Sản phẩm</title>
<section class="card">
    <h1>Tất cả sản phẩm</h1>
    <p>Hiển thị 6 sản phẩm trên mỗi trang. Tổng số: <c:out value="${total}"/>.</p>
    <div class="product-grid">
        <c:forEach var="product" items="${products}">
            <c:url var="detailUrl" value="/product/detail">
                <c:param name="id" value="${product.productId}"/>
            </c:url>
            <article class="product-card">
                <a href="${detailUrl}">
                    <c:choose>
                        <c:when test="${not empty product.images && fn:startsWith(product.images, 'https://')}">
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
                <p><fmt:formatNumber value="${product.price}" type="number"/> đ</p>
                <p class="muted"><c:out value="${product.category.categoryname}"/></p>
            </article>
        </c:forEach>
        <c:if test="${empty products}">
            <p>Chưa có sản phẩm nào.</p>
        </c:if>
    </div>
    <c:if test="${totalPages > 1}">
        <nav class="pagination" aria-label="Phân trang sản phẩm">
            <c:forEach var="i" begin="1" end="${totalPages}">
                <c:url var="pageUrl" value="/product">
                    <c:param name="page" value="${i}"/>
                </c:url>
                <c:choose>
                    <c:when test="${i == page}"><strong>${i}</strong></c:when>
                    <c:otherwise><a href="${pageUrl}">${i}</a></c:otherwise>
                </c:choose>
            </c:forEach>
        </nav>
    </c:if>
</section>
