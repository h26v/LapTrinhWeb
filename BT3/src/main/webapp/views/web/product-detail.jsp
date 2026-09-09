<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<title><c:out value="${product.productName}"/></title>
<section class="card product-detail">
    <div>
        <c:choose>
            <c:when test="${not empty product.images && fn:startsWith(product.images, 'https://')}">
                <img src="<c:out value='${product.images}'/>" alt="${product.productName}"/>
            </c:when>
            <c:when test="${not empty product.images}">
                <c:url var="imageUrl" value="/image">
                    <c:param name="fname" value="${product.images}"/>
                </c:url>
                <img src="${imageUrl}" alt="${product.productName}"/>
            </c:when>
            <c:otherwise><div class="product-placeholder">No image</div></c:otherwise>
        </c:choose>
    </div>
    <div>
        <h1><c:out value="${product.productName}"/></h1>
        <p class="price"><fmt:formatNumber value="${product.price}" type="number"/> đ</p>
        <p>Danh mục: <strong><c:out value="${product.category.categoryname}"/></strong></p>
        <p><c:out value="${product.description}"/></p>
        <p><a href="<c:url value='/product'/>">Quay lại danh sách sản phẩm</a></p>
    </div>
</section>
