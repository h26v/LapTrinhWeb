<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<title>Sửa danh mục</title>
<section class="card">
    <h1>Sửa danh mục</h1>
    <form action="<c:url value='/admin/category/update'/>" method="post" enctype="multipart/form-data">
        <input type="hidden" name="categoryid" value="<c:out value='${category.categoryid}'/>" />
        <label for="categoryname">Tên danh mục</label>
        <input type="text" id="categoryname" name="categoryname" value="<c:out value='${category.categoryname}'/>" required />
        <label>Ảnh hiện tại</label>
        <c:choose>
            <c:when test="${empty category.images}"><span>Chưa có ảnh</span></c:when>
            <c:when test="${fn:startsWith(category.images, 'https://')}"><img class="icon" src="<c:out value='${category.images}'/>" alt="icon"/></c:when>
            <c:otherwise><img class="icon" src="<c:url value='/image'><c:param name='fname' value='${category.images}'/></c:url>" alt="icon"/></c:otherwise>
        </c:choose>
        <label for="images1">Chọn ảnh mới (bỏ trống để giữ ảnh cũ)</label>
        <input type="file" id="images1" name="images1" accept="image/*" />
        <label>Trạng thái</label>
        <input type="radio" id="status1" name="status" value="1" ${category.status == 1 ? 'checked' : ''}/>
        <label for="status1" style="display:inline;font-weight:normal;">Hoạt động</label>
        <input type="radio" id="status0" name="status" value="0" ${category.status == 0 ? 'checked' : ''}/>
        <label for="status0" style="display:inline;font-weight:normal;">Khóa</label>
        <div style="margin-top:20px;"><button type="submit">Cập nhật</button> <a href="<c:url value='/admin/categories'/>">Quay lại</a></div>
    </form>
</section>
