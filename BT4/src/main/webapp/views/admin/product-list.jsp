<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<title>Danh sách sản phẩm</title>
<section class="card border-0">
    <div class="card-body p-4">
        <div class="d-flex justify-content-between align-items-center gap-3 flex-wrap mb-3">
            <div>
                <h1 class="h3 mb-1">Danh sách sản phẩm</h1>
                <p class="text-muted mb-0">Tổng số sản phẩm: <c:out value="${total}"/></p>
            </div>
            <div class="d-flex gap-2 flex-wrap">
                <a class="btn btn-primary" href="<c:url value='/admin/product/add'/>">Thêm sản phẩm</a>
                <a class="btn btn-outline-secondary" href="<c:url value='/admin/categories'/>">Quản lý danh mục</a>
            </div>
        </div>
        <c:if test="${not empty sessionScope.alert}">
            <div class="alert alert-error"><c:out value="${sessionScope.alert}"/></div>
            <c:remove var="alert" scope="session"/>
        </c:if>
        <div class="table-responsive">
            <table class="table table-bordered table-hover align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th>ID</th><th>Tên sản phẩm</th><th>Ảnh</th><th>Giá</th><th>Danh mục</th><th>Trạng thái</th><th>Hành động</th>
                    </tr>
                </thead>
                <tbody>
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
                                        <c:when test="${fn:startsWith(product.images, 'https://') || fn:startsWith(product.images, 'http://')}">
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
                            <td>
                                <c:choose>
                                    <c:when test="${product.status == 1}"><span class="badge text-bg-success">Hiển thị</span></c:when>
                                    <c:otherwise><span class="badge text-bg-secondary">Ẩn</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a class="btn btn-sm btn-outline-secondary" href="${detailUrl}">Xem</a>
                                <a class="btn btn-sm btn-outline-primary" href="${editUrl}">Sửa</a>
                                <a class="btn btn-sm btn-outline-danger" href="${deleteUrl}" onclick="return confirm('Xóa sản phẩm này?');">Xóa</a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty products}">
                        <tr><td colspan="7" class="text-center text-muted">Chưa có sản phẩm nào.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</section>
