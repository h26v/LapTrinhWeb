<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<title>Thêm sản phẩm</title>
<section class="card border-0 mx-auto" style="max-width: 760px;">
    <div class="card-body p-4">
        <div class="d-flex justify-content-between align-items-center gap-3 flex-wrap mb-3">
            <h1 class="h3 mb-0">Thêm sản phẩm</h1>
            <a class="btn btn-outline-secondary" href="<c:url value='/admin/products'/>">Quay lại</a>
        </div>
        <c:if test="${not empty alert}">
            <div class="alert alert-error" role="alert"><c:out value="${alert}"/></div>
        </c:if>
        <c:if test="${empty categories}">
            <div class="alert alert-error">Vui lòng tạo danh mục trước khi thêm sản phẩm.</div>
        </c:if>
        <form action="<c:url value='/admin/product/insert'/>" method="post" enctype="multipart/form-data">
            <div class="mb-3">
                <label class="form-label" for="productname">Tên sản phẩm</label>
                <input class="form-control" type="text" id="productname" name="productname" required maxlength="255"
                       value="<c:out value='${product.productName}'/>"/>
            </div>

            <div class="mb-3">
                <label class="form-label" for="price">Giá</label>
                <input class="form-control" type="number" id="price" name="price" required min="0" step="0.01"
                       value="<c:out value='${priceInput != null ? priceInput : product.price}'/>"/>
                <div class="form-text">Không âm, tối đa 10 chữ số phần nguyên và 2 chữ số thập phân.</div>
            </div>

            <div class="mb-3">
                <label class="form-label" for="categoryid">Danh mục</label>
                <select class="form-select" id="categoryid" name="categoryid" required ${empty categories ? 'disabled' : ''}>
                    <option value="">-- Chọn danh mục --</option>
                    <c:forEach var="cate" items="${categories}">
                        <option value="${cate.categoryid}" ${not empty product.category && product.category.categoryid == cate.categoryid ? 'selected' : ''}>
                            <c:out value="${cate.categoryname}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="mb-3">
                <label class="form-label" for="description">Mô tả</label>
                <textarea class="form-control" id="description" name="description" rows="5" maxlength="1000"><c:out value="${product.description}"/></textarea>
            </div>

            <div class="mb-3">
                <label class="form-label" for="images">Ảnh sản phẩm</label>
                <input class="form-control" type="file" id="images" name="images"
                       accept="image/png,image/jpeg,image/gif,image/bmp"/>
                <div class="form-text">PNG, JPEG, GIF hoặc BMP; tối đa 5 MB.</div>
            </div>

            <div class="mb-4">
                <label class="form-label" for="status">Trạng thái</label>
                <select class="form-select" id="status" name="status">
                    <option value="1" ${empty product || product.status == 1 ? 'selected' : ''}>Hiển thị</option>
                    <option value="0" ${not empty product && product.status == 0 ? 'selected' : ''}>Ẩn</option>
                </select>
            </div>

            <button class="btn btn-primary" type="submit" ${empty categories ? 'disabled' : ''}>Lưu sản phẩm</button>
        </form>
    </div>
</section>
