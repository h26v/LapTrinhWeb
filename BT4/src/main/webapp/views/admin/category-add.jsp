<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<title>Thêm danh mục</title>
<section class="card border-0 mx-auto" style="max-width: 680px;">
    <div class="card-body p-4">
        <div class="d-flex justify-content-between align-items-center gap-3 flex-wrap mb-3">
            <h1 class="h3 mb-0">Thêm danh mục</h1>
            <a class="btn btn-outline-secondary" href="<c:url value='/admin/categories'/>">Quay lại</a>
        </div>
        <c:if test="${not empty alert}">
            <div class="alert alert-error" role="alert"><c:out value="${alert}"/></div>
        </c:if>
        <form action="<c:url value='/admin/category/insert'/>" method="post" enctype="multipart/form-data">
            <div class="mb-3">
                <label class="form-label" for="categoryname">Tên danh mục</label>
                <input class="form-control" type="text" id="categoryname" name="categoryname" required maxlength="255"
                       value="<c:out value='${category.categoryname}'/>"/>
            </div>
            <div class="mb-3">
                <label class="form-label" for="images1">Ảnh danh mục</label>
                <input class="form-control" type="file" id="images1" name="images1"
                       accept="image/png,image/jpeg,image/gif,image/bmp"/>
                <div class="form-text">PNG, JPEG, GIF hoặc BMP; tối đa 5 MB.</div>
            </div>
            <div class="mb-4">
                <label class="form-label d-block">Trạng thái</label>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" id="status1" name="status" value="1"
                           ${empty category || category.status == 1 ? 'checked' : ''}/>
                    <label class="form-check-label" for="status1">Hoạt động</label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" id="status0" name="status" value="0"
                           ${not empty category && category.status == 0 ? 'checked' : ''}/>
                    <label class="form-check-label" for="status0">Khóa</label>
                </div>
            </div>
            <button class="btn btn-primary" type="submit">Lưu danh mục</button>
        </form>
    </div>
</section>
