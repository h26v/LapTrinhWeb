<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<title>Thêm sản phẩm</title>
<section class="card" style="max-width: 720px; margin: 0 auto;">
    <h1>Thêm sản phẩm</h1>
    <c:if test="${not empty alert}">
        <p class="alert alert-error" role="alert"><c:out value="${alert}"/></p>
    </c:if>
    <c:if test="${empty categories}">
        <p class="alert alert-error">Vui lòng tạo danh mục trước khi thêm sản phẩm.</p>
    </c:if>
    <form action="<c:url value='/admin/product/insert'/>" method="post" enctype="multipart/form-data">
        <label for="productname">Tên sản phẩm</label>
        <input type="text" id="productname" name="productname" required maxlength="255"
               style="width:100%;padding:10px;border:1px solid #c8d3e2;border-radius:6px;"/>

        <label for="price">Giá</label>
        <input type="number" id="price" name="price" required min="0" step="1000"
               style="width:100%;padding:10px;border:1px solid #c8d3e2;border-radius:6px;"/>

        <label for="categoryid">Danh mục</label>
        <select id="categoryid" name="categoryid" required
                style="width:100%;padding:10px;border:1px solid #c8d3e2;border-radius:6px;">
            <option value="">-- Chọn danh mục --</option>
            <c:forEach var="cate" items="${categories}">
                <option value="${cate.categoryid}"><c:out value="${cate.categoryname}"/></option>
            </c:forEach>
        </select>

        <label for="description">Mô tả</label>
        <textarea id="description" name="description" rows="5"
                  style="width:100%;padding:10px;border:1px solid #c8d3e2;border-radius:6px;"></textarea>

        <label for="images">Ảnh sản phẩm</label>
        <input type="file" id="images" name="images" accept="image/*"/>

        <label for="status">Trạng thái</label>
        <select id="status" name="status"
                style="width:100%;padding:10px;border:1px solid #c8d3e2;border-radius:6px;">
            <option value="1">Hiển thị</option>
            <option value="0">Ẩn</option>
        </select>

        <div style="margin-top:16px;">
            <button type="submit">Lưu</button>
            <a href="<c:url value='/admin/products'/>">Quay lại</a>
        </div>
    </form>
</section>
