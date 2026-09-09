<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<title>Sửa sản phẩm</title>
<section class="card" style="max-width: 720px; margin: 0 auto;">
    <h1>Sửa sản phẩm</h1>
    <c:if test="${not empty alert}">
        <p class="alert alert-error" role="alert"><c:out value="${alert}"/></p>
    </c:if>
    <form action="<c:url value='/admin/product/update'/>" method="post" enctype="multipart/form-data">
        <input type="hidden" name="productid" value="${product.productId}"/>

        <label for="productname">Tên sản phẩm</label>
        <input type="text" id="productname" name="productname" required maxlength="255"
               value="<c:out value='${product.productName}'/>"
               style="width:100%;padding:10px;border:1px solid #c8d3e2;border-radius:6px;"/>

        <label for="price">Giá</label>
        <input type="number" id="price" name="price" required min="0" step="1000"
               value="${product.price}"
               style="width:100%;padding:10px;border:1px solid #c8d3e2;border-radius:6px;"/>

        <label for="categoryid">Danh mục</label>
        <select id="categoryid" name="categoryid" required
                style="width:100%;padding:10px;border:1px solid #c8d3e2;border-radius:6px;">
            <c:forEach var="cate" items="${categories}">
                <option value="${cate.categoryid}" ${product.category.categoryid == cate.categoryid ? 'selected' : ''}><c:out value="${cate.categoryname}"/></option>
            </c:forEach>
        </select>

        <label for="description">Mô tả</label>
        <textarea id="description" name="description" rows="5"
                  style="width:100%;padding:10px;border:1px solid #c8d3e2;border-radius:6px;"><c:out value="${product.description}"/></textarea>

        <c:if test="${not empty product.images}">
            <p>Ảnh hiện tại:</p>
            <c:choose>
                <c:when test="${fn:startsWith(product.images, 'https://')}">
                    <img src="<c:out value='${product.images}'/>" alt="${product.productName}" style="max-width:160px;border-radius:8px;"/>
                </c:when>
                <c:otherwise>
                    <c:url var="imageUrl" value="/image">
                        <c:param name="fname" value="${product.images}"/>
                    </c:url>
                    <img src="${imageUrl}" alt="${product.productName}" style="max-width:160px;border-radius:8px;"/>
                </c:otherwise>
            </c:choose>
        </c:if>
        <label for="images">Ảnh mới</label>
        <input type="file" id="images" name="images" accept="image/*"/>

        <label for="status">Trạng thái</label>
        <select id="status" name="status"
                style="width:100%;padding:10px;border:1px solid #c8d3e2;border-radius:6px;">
            <option value="1" ${product.status == 1 ? 'selected' : ''}>Hiển thị</option>
            <option value="0" ${product.status == 0 ? 'selected' : ''}>Ẩn</option>
        </select>

        <div style="margin-top:16px;">
            <button type="submit">Cập nhật</button>
            <a href="<c:url value='/admin/products'/>">Quay lại</a>
        </div>
    </form>
</section>
