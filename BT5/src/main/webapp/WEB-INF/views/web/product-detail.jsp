<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="${product.productName}" scope="request"/>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>

<div class="mb-3">
    <a class="btn btn-outline-secondary btn-sm" href="<c:url value='/product'/>">← Danh sách sản phẩm</a>
</div>

<section class="card border-0">
    <div class="card-body p-4">
        <div class="product-detail">
            <div>
                <c:choose>
                    <c:when test="${empty product.images}">
                        <div class="product-placeholder">Chưa có ảnh</div>
                    </c:when>
                    <c:when test="${fn:startsWith(product.images, 'https://') || fn:startsWith(product.images, 'http://')}">
                        <img src="<c:out value='${product.images}'/>" alt="Ảnh sản phẩm"/>
                    </c:when>
                    <c:otherwise>
                        <c:url var="imageUrl" value="/image">
                            <c:param name="fname" value="${product.images}"/>
                        </c:url>
                        <img src="${imageUrl}" alt="Ảnh sản phẩm"/>
                    </c:otherwise>
                </c:choose>
            </div>
            <div>
                <h1 class="h3 mb-2"><c:out value="${product.productName}"/></h1>
                <p class="text-muted">
                    Danh mục: <c:out value="${product.category.categoryname}"/>
                </p>
                <p class="price"><fmt:formatNumber value="${product.price}" type="number" groupingUsed="true"/> đ</p>
                <p class="mt-3"><c:out value="${product.description}"/></p>
                <p class="text-muted small">
                    Ngày tạo:
                    <fmt:formatDate value="${product.createdDate}" pattern="dd/MM/yyyy HH:mm"/>
                </p>
            </div>
        </div>
    </div>
</section>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
