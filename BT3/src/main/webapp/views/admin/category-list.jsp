<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<title>Danh sách danh mục</title>
<style>
    table { border-collapse: collapse; width: 100%; background: #fff; }
    th, td { border: 1px solid #dce4ef; padding: 10px; text-align: left; }
    th { background: #eef3f9; }
    img.icon { height: 40px; width: 40px; object-fit: cover; }
    .toolbar { margin-bottom: 18px; display: flex; gap: 12px; flex-wrap: wrap; align-items: center; }
</style>
<section class="card">
    <h1>Danh sách danh mục (JPA)</h1>
    <c:if test="${not empty sessionScope.alert}">
        <p class="alert alert-error"><c:out value="${sessionScope.alert}"/></p>
        <c:remove var="alert" scope="session"/>
    </c:if>
    <div class="toolbar">
        <a href="<c:url value='/admin/category/add'/>">Thêm danh mục</a>
        <form action="<c:url value='/admin/categories'/>" method="get">
            <input type="text" name="keyword" value="<c:out value='${keyword}'/>" placeholder="Tìm theo tên..."/>
            <button type="submit">Tìm</button>
        </form>
    </div>
    <p>Tổng số danh mục: <c:out value="${total}"/></p>
    <table>
        <tr><th>ID</th><th>Tên danh mục</th><th>Ảnh</th><th>Trạng thái</th><th>Hành động</th></tr>
        <c:forEach var="cate" items="${categories}">
            <tr>
                <td><c:out value="${cate.categoryid}"/></td>
                <td><c:out value="${cate.categoryname}"/></td>
                <td>
                    <c:if test="${not empty cate.images}">
                        <c:choose>
                            <c:when test="${fn:startsWith(cate.images, 'https://')}"><img class="icon" src="<c:out value='${cate.images}'/>" alt="icon"/></c:when>
                            <c:otherwise><img class="icon" src="<c:url value='/image'><c:param name='fname' value='${cate.images}'/></c:url>" alt="icon"/></c:otherwise>
                        </c:choose>
                    </c:if>
                </td>
                <td><c:choose><c:when test="${cate.status == 1}">Hoạt động</c:when><c:otherwise>Khóa</c:otherwise></c:choose></td>
                <td>
                    <a href="<c:url value='/admin/category/edit'><c:param name='id' value='${cate.categoryid}'/></c:url>">Sửa</a>
                    · <a href="<c:url value='/admin/category/delete'><c:param name='id' value='${cate.categoryid}'/></c:url>" onclick="return confirm('Xóa danh mục này?');">Xóa</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty categories}"><tr><td colspan="5">Chưa có danh mục nào.</td></tr></c:if>
    </table>
</section>
