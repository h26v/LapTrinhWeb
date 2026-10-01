<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head><title>Quản lý Sách</title></head>
<body>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h3 class="mb-0">Quản lý Sách</h3>
    <a href="${ctx}/admin/books/add" class="btn btn-primary"><i class="bi bi-plus-lg"></i> Thêm sách</a>
</div>

<c:if test="${not empty sessionScope.flash}">
    <div class="alert alert-success"><c:out value="${sessionScope.flash}"/></div>
    <c:remove var="flash" scope="session"/>
</c:if>

<div class="table-responsive">
    <table class="table table-hover align-middle bg-white">
        <thead class="table-light">
        <tr>
            <th>ID</th><th>Bìa</th><th>Tiêu đề</th><th>ISBN</th><th>Tác giả</th>
            <th>Publisher</th><th>Giá</th><th>Ngày XB</th><th>SL</th><th>Reviews</th><th class="text-end">Thao tác</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="b" items="${result.items}">
            <tr>
                <td>${b.bookid}</td>
                <td><t:cover src="${b.coverImage}" alt="${b.title}" cssClass="thumb rounded"/></td>
                <td><c:out value="${b.title}"/></td>
                <td>${b.isbn}</td>
                <td><c:out value="${b.authorNames}"/></td>
                <td><c:out value="${b.publisher}"/></td>
                <td>${b.price}</td>
                <td>${b.publishDate}</td>
                <td>${b.quantity}</td>
                <td>${b.reviewCount}</td>
                <td class="text-end text-nowrap">
                    <a class="btn btn-sm btn-outline-secondary" href="${ctx}/book/detail?id=${b.bookid}" title="Xem">
                        <i class="bi bi-eye"></i></a>
                    <a class="btn btn-sm btn-outline-primary" href="${ctx}/admin/books/edit?id=${b.bookid}" title="Sửa">
                        <i class="bi bi-pencil"></i></a>
                    <form method="post" action="${ctx}/admin/books/delete" class="d-inline"
                          onsubmit="return confirm('Xóa sách này (kèm reviews)?')">
                        <input type="hidden" name="id" value="${b.bookid}">
                        <input type="hidden" name="page" value="${result.page}">
                        <button class="btn btn-sm btn-outline-danger" title="Xóa"><i class="bi bi-trash"></i></button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty result.items}">
            <tr><td colspan="11" class="text-center text-secondary">Chưa có sách.</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/includes/pagination.jspf" %>
</body>
</html>
