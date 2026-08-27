<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Danh sách danh mục</title>
<style>
body { font-family: Arial, sans-serif; margin: 24px; }
table { border-collapse: collapse; width: 100%; }
th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
th { background: #f2f2f2; }
img.icon { height: 40px; }
.alert { color: #b00; margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
</style>
</head>
<body>
	<h2>Danh sách danh mục (JPA)</h2>

	<c:if test="${not empty sessionScope.alert}">
		<p class="alert">${sessionScope.alert}</p>
		<c:remove var="alert" scope="session" />
	</c:if>

	<div class="toolbar">
		<a href="<c:url value='/admin/category/add'/>">Thêm danh mục</a>
		| <a href="<c:url value='/'/>">Trang chủ</a>
		<form action="<c:url value='/admin/categories'/>" method="get"
			style="display: inline; margin-left: 16px;">
			<input type="text" name="keyword" value="${keyword}"
				placeholder="Tìm theo tên..." />
			<button type="submit">Tìm</button>
		</form>
	</div>

	<p>Tổng số danh mục: ${total}</p>

	<table>
		<tr>
			<th>ID</th>
			<th>Tên danh mục</th>
			<th>Ảnh</th>
			<th>Trạng thái</th>
			<th>Hành động</th>
		</tr>
		<c:forEach var="cate" items="${categories}">
			<tr>
				<td>${cate.categoryid}</td>
				<td>${cate.categoryname}</td>
				<td><c:if test="${not empty cate.images}">
						<c:choose>
							<c:when test="${fn:startsWith(cate.images, 'https')}">
								<img class="icon" src="${cate.images}" alt="icon" />
							</c:when>
							<c:otherwise>
								<img class="icon"
									src="<c:url value='/image'><c:param name='fname' value='${cate.images}'/></c:url>"
									alt="icon" />
							</c:otherwise>
						</c:choose>
					</c:if></td>
				<td><c:choose>
						<c:when test="${cate.status == 1}">Hoạt động</c:when>
						<c:otherwise>Khóa</c:otherwise>
					</c:choose></td>
				<td><a
					href="<c:url value='/admin/category/edit'><c:param name='id' value='${cate.categoryid}'/></c:url>">Sửa</a>
					| <a
					href="<c:url value='/admin/category/delete'><c:param name='id' value='${cate.categoryid}'/></c:url>"
					onclick="return confirm('Xóa danh mục này?');">Xóa</a></td>
			</tr>
		</c:forEach>
		<c:if test="${empty categories}">
			<tr>
				<td colspan="5">Chưa có danh mục nào.</td>
			</tr>
		</c:if>
	</table>
</body>
</html>
