<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Trang chủ" scope="request"/>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>

<section class="mb-4">
    <h1 class="h3 mb-1">Trang chủ</h1>
    <p class="text-muted mb-0">Sản phẩm mới nhất của cửa hàng.</p>
</section>

<div class="product-grid">
    <c:forEach var="p" items="${products}">
        <article class="card product-card border-0">
            <c:url var="detailUrl" value="/product/detail">
                <c:param name="id" value="${p.productId}"/>
            </c:url>
            <a href="${detailUrl}">
                <c:choose>
                    <c:when test="${empty p.images}">
                        <div class="product-placeholder">Chưa có ảnh</div>
                    </c:when>
                    <c:when test="${fn:startsWith(p.images, 'https://') || fn:startsWith(p.images, 'http://')}">
                        <img src="<c:out value='${p.images}'/>" alt="Ảnh sản phẩm"/>
                    </c:when>
                    <c:otherwise>
                        <c:url var="imageUrl" value="/image">
                            <c:param name="fname" value="${p.images}"/>
                        </c:url>
                        <img src="${imageUrl}" alt="Ảnh sản phẩm"/>
                    </c:otherwise>
                </c:choose>
                <h2><c:out value="${p.productName}"/></h2>
                <p class="text-muted small"><c:out value="${p.category.categoryname}"/></p>
                <p class="price"><fmt:formatNumber value="${p.price}" type="number" groupingUsed="true"/> đ</p>
            </a>
        </article>
    </c:forEach>
</div>

<c:if test="${empty products}">
    <div class="alert alert-info">Chưa có sản phẩm nào đang bán.</div>
</c:if>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
