<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Thêm danh mục</title>
<style>
body { font-family: Arial, sans-serif; margin: 24px; }
label { display: block; margin-top: 12px; font-weight: bold; }
input[type=text] { width: 320px; padding: 6px; }
.actions { margin-top: 16px; }
</style>
</head>
<body>
	<h2>Thêm danh mục</h2>

	<form action="<c:url value='/admin/category/insert'/>" method="post"
		enctype="multipart/form-data">
		<label for="categoryname">Tên danh mục</label>
		<input type="text" id="categoryname" name="categoryname" required />

		<label for="images1">Ảnh danh mục</label>
		<input type="file" id="images1" name="images1" accept="image/*" />

		<label>Trạng thái</label>
		<input type="radio" id="status1" name="status" value="1" checked />
		<label for="status1" style="display: inline; font-weight: normal;">Hoạt động</label>
		<input type="radio" id="status0" name="status" value="0" />
		<label for="status0" style="display: inline; font-weight: normal;">Khóa</label>

		<div class="actions">
			<button type="submit">Lưu</button>
			<a href="<c:url value='/admin/categories'/>">Quay lại</a>
		</div>
	</form>
</body>
</html>
