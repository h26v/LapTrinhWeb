<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<title>Thêm danh mục</title>
<section class="card">
    <h1>Thêm danh mục</h1>
    <form action="<c:url value='/admin/category/insert'/>" method="post" enctype="multipart/form-data">
        <label for="categoryname">Tên danh mục</label>
        <input type="text" id="categoryname" name="categoryname" required />
        <label for="images1">Ảnh danh mục</label>
        <input type="file" id="images1" name="images1" accept="image/*" />
        <label>Trạng thái</label>
        <input type="radio" id="status1" name="status" value="1" checked />
        <label for="status1" style="display:inline;font-weight:normal;">Hoạt động</label>
        <input type="radio" id="status0" name="status" value="0" />
        <label for="status0" style="display:inline;font-weight:normal;">Khóa</label>
        <div style="margin-top:20px;"><button type="submit">Lưu</button> <a href="<c:url value='/admin/categories'/>">Quay lại</a></div>
    </form>
</section>
