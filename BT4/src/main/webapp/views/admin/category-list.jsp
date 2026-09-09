<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<title>Danh sách danh mục</title>
<section class="card border-0">
    <div class="card-body p-4">
        <div class="d-flex justify-content-between align-items-center gap-3 flex-wrap mb-3">
            <div>
                <h1 class="h3 mb-1">Danh sách danh mục</h1>
                <p class="text-muted mb-0">Tổng số danh mục: <c:out value="${total}"/></p>
            </div>
            <a class="btn btn-primary" href="<c:url value='/admin/category/add'/>">Thêm danh mục</a>
        </div>

        <c:if test="${not empty sessionScope.alert}">
            <div class="alert alert-error"><c:out value="${sessionScope.alert}"/></div>
            <c:remove var="alert" scope="session"/>
        </c:if>
        <c:if test="${not empty alert}">
            <div class="alert alert-error"><c:out value="${alert}"/></div>
        </c:if>

        <form class="row g-2 mb-3" action="<c:url value='/admin/categories'/>" method="get">
            <div class="col-sm-8 col-md-5">
                <input class="form-control" type="text" name="keyword" maxlength="100"
                       value="<c:out value='${keyword}'/>" placeholder="Tìm theo tên..."/>
            </div>
            <div class="col-auto">
                <button class="btn btn-outline-primary" type="submit">Tìm</button>
            </div>
        </form>

        <div class="table-responsive">
            <table class="table table-bordered table-hover align-middle mb-0">
                <thead class="table-light">
                    <tr><th>ID</th><th>Tên danh mục</th><th>Ảnh</th><th>Trạng thái</th><th>Hành động</th></tr>
                </thead>
                <tbody>
                    <c:forEach var="cate" items="${categories}">
                        <c:url var="editUrl" value="/admin/category/edit">
                            <c:param name="id" value="${cate.categoryid}"/>
                        </c:url>
                        <c:url var="deleteUrl" value="/admin/category/delete">
                            <c:param name="id" value="${cate.categoryid}"/>
                        </c:url>
                        <tr>
                            <td><c:out value="${cate.categoryid}"/></td>
                            <td><c:out value="${cate.categoryname}"/></td>
                            <td>
                                <c:if test="${not empty cate.images}">
                                    <c:choose>
                                        <c:when test="${fn:startsWith(cate.images, 'https://') || fn:startsWith(cate.images, 'http://')}">
                                            <img class="icon" src="<c:out value='${cate.images}'/>" alt="icon"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:url var="imageUrl" value="/image">
                                                <c:param name="fname" value="${cate.images}"/>
                                            </c:url>
                                            <img class="icon" src="${imageUrl}" alt="icon"/>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${cate.status == 1}"><span class="badge text-bg-success">Hoạt động</span></c:when>
                                    <c:otherwise><span class="badge text-bg-secondary">Khóa</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a class="btn btn-sm btn-outline-primary" href="${editUrl}">Sửa</a>
                                <a class="btn btn-sm btn-outline-danger" href="${deleteUrl}"
                                   onclick="return confirm('Xóa danh mục này?');">Xóa</a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty categories}">
                        <tr><td colspan="5" class="text-center text-muted">Chưa có danh mục nào.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</section>
