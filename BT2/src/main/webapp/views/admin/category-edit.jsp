<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sửa danh mục</title>
<style>
body { font-family: Arial, sans-serif; margin: 24px; }
label { display: block; margin-top: 12px; font-weight: bold; }
input[type=text] { width: 320px; padding: 6px; }
img.icon { height: 60px; display: block; margin-top: 6px; }
.actions { margin-top: 16px; }
</style>
</head>
<body>
	<h2>Sửa danh mục</h2>

	<form action="<c:url value='/admin/category/update'/>" method="post"
		enctype="multipart/form-data">
		<input type="hidden" name="categoryid" value="${category.categoryid}" />

		<label for="categoryname">Tên danh mục</label>
		<input type="text" id="categoryname" name="categoryname"
			value="${category.categoryname}" required />

		<label>Ảnh hiện tại</label>
		<c:choose>
			<c:when test="${empty category.images}">
				<span>Chưa có ảnh</span>
			</c:when>
			<c:when test="${fn:startsWith(category.images, 'https')}">
				<img class="icon" src="${category.images}" alt="icon" />
			</c:when>
			<c:otherwise>
				<img class="icon"
					src="<c:url value='/image'><c:param name='fname' value='${category.images}'/></c:url>"
					alt="icon" />
			</c:otherwise>
		</c:choose>

		<label for="images1">Chọn ảnh mới (bỏ trống để giữ ảnh cũ)</label>
		<input type="file" id="images1" name="images1" accept="image/*" />

		<label>Trạng thái</label>
		<input type="radio" id="status1" name="status" value="1"
			${category.status == 1 ? 'checked' : ''} />
		<label for="status1" style="display: inline; font-weight: normal;">Hoạt động</label>
		<input type="radio" id="status0" name="status" value="0"
			${category.status == 0 ? 'checked' : ''} />
		<label for="status0" style="display: inline; font-weight: normal;">Khóa</label>

		<div class="actions">
			<button type="submit">Cập nhật</button>
			<a href="<c:url value='/admin/categories'/>">Quay lại</a>
		</div>
	</form>
</body>
</html>
