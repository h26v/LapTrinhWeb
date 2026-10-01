<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Quản lý Tác giả</title></head>
<body>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h3 class="mb-0">Quản lý Tác giả</h3>
    <a href="${ctx}/admin/authors/add" class="btn btn-primary"><i class="bi bi-plus-lg"></i> Thêm tác giả</a>
</div>

<c:if test="${not empty sessionScope.flash}">
    <div class="alert alert-success"><c:out value="${sessionScope.flash}"/></div>
    <c:remove var="flash" scope="session"/>
</c:if>

<table class="table table-hover align-middle bg-white">
    <thead class="table-light">
    <tr><th>ID</th><th>Tên tác giả</th><th>Ngày sinh</th><th class="text-end">Thao tác</th></tr>
    </thead>
    <tbody>
    <c:forEach var="a" items="${result.items}">
        <tr>
            <td>${a.authorId}</td>
            <td><c:out value="${a.authorName}"/></td>
            <td>${a.dateOfBirth}</td>
            <td class="text-end text-nowrap">
                <a class="btn btn-sm btn-outline-primary" href="${ctx}/admin/authors/edit?id=${a.authorId}" title="Sửa">
                    <i class="bi bi-pencil"></i></a>
                <form method="post" action="${ctx}/admin/authors/delete" class="d-inline"
                      onsubmit="return confirm('Xóa tác giả này?')">
                    <input type="hidden" name="id" value="${a.authorId}">
                    <input type="hidden" name="page" value="${result.page}">
                    <button class="btn btn-sm btn-outline-danger" title="Xóa"><i class="bi bi-trash"></i></button>
                </form>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty result.items}">
        <tr><td colspan="4" class="text-center text-secondary">Chưa có tác giả.</td></tr>
    </c:if>
    </tbody>
</table>

<%@ include file="/WEB-INF/includes/pagination.jspf" %>
</body>
</html>
