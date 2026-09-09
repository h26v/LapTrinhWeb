<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<title>Danh sách sản phẩm</title>
<style>
    table { border-collapse: collapse; width: 100%; background: #fff; }
    th, td { border: 1px solid #dce4ef; padding: 10px; text-align: left; vertical-align: top; }
    th { background: #eef3f9; }
    img.icon { height: 56px; width: 56px; object-fit: cover; border-radius: 8px; }
    .toolbar { margin-bottom: 18px; display: flex; gap: 12px; flex-wrap: wrap; align-items: center; }
</style>
<section class="card">
    <h1>Danh sách sản phẩm</h1>
    <c:if test="${not empty sessionScope.alert}">
        <p class="alert alert-error"><c:out value="${sessionScope.alert}"/></p>
        <c:remove var="alert" scope="session"/>
    </c:if>
    <div class="toolbar">
        <a href="<c:url value='/admin/product/add'/>">Thêm sản phẩm</a>
        <a href="<c:url value='/admin/categories'/>">Quản lý danh mục</a>
    </div>
    <p>Tổng số sản phẩm: <c:out value="${total}"/></p>
    <table>
        <tr>
            <th>ID</th><th>Tên sản phẩm</th><th>Ảnh</th><th>Giá</th><th>Danh mục</th><th>Trạng thái</th><th>Hành động</th>
        </tr>
        <c:forEach var="product" items="${products}">
            <c:url var="detailUrl" value="/product/detail">
                <c:param name="id" value="${product.productId}"/>
            </c:url>
            <c:url var="editUrl" value="/admin/product/edit">
                <c:param name="id" value="${product.productId}"/>
            </c:url>
            <c:url var="deleteUrl" value="/admin/product/delete">
                <c:param name="id" value="${product.productId}"/>
            </c:url>
            <tr>
                <td><c:out value="${product.productId}"/></td>
                <td><c:out value="${product.productName}"/></td>
                <td>
                    <c:if test="${not empty product.images}">
                        <c:choose>
                            <c:when test="${fn:startsWith(product.images, 'https://')}">
                                <img class="icon" src="<c:out value='${product.images}'/>" alt="${product.productName}"/>
                            </c:when>
                            <c:otherwise>
                                <c:url var="imageUrl" value="/image">
                                    <c:param name="fname" value="${product.images}"/>
                                </c:url>
                                <img class="icon" src="${imageUrl}" alt="${product.productName}"/>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </td>
                <td><fmt:formatNumber value="${product.price}" type="number"/> đ</td>
                <td><c:out value="${product.category.categoryname}"/></td>
                <td><c:choose><c:when test="${product.status == 1}">Hiển thị</c:when><c:otherwise>Ẩn</c:otherwise></c:choose></td>
                <td>
                    <a href="${detailUrl}">Xem</a>
                    · <a href="${editUrl}">Sửa</a>
                    · <a href="${deleteUrl}" onclick="return confirm('Xóa sản phẩm này?');">Xóa</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty products}"><tr><td colspan="7">Chưa có sản phẩm nào.</td></tr></c:if>
    </table>
</section>
