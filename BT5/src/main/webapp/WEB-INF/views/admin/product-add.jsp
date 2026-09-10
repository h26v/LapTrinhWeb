<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Thêm sản phẩm" scope="request"/>
<c:set var="adminTab" value="product" scope="request"/>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>

<section class="card border-0 mx-auto" style="max-width: 720px;">
    <div class="card-body p-4">
        <div class="d-flex justify-content-between align-items-center gap-3 flex-wrap mb-3">
            <h1 class="h3 mb-0">Thêm sản phẩm</h1>
            <a class="btn btn-outline-secondary" href="<c:url value='/admin/products'/>">Quay lại</a>
        </div>

        <jsp:include page="/WEB-INF/views/layout/messages.jsp"/>

        <form action="<c:url value='/admin/product/insert'/>" method="post" enctype="multipart/form-data">
            <div class="mb-3">
                <label class="form-label" for="productname">Tên sản phẩm <span class="text-danger">*</span></label>
                <input class="form-control" type="text" id="productname" name="productname" required maxlength="255"
                       value="<c:out value='${product.productName}'/>"/>
            </div>
            <div class="mb-3">
                <label class="form-label" for="description">Mô tả</label>
                <textarea class="form-control" id="description" name="description" rows="3"
                          maxlength="1000"><c:out value="${product.description}"/></textarea>
            </div>
            <div class="row g-3">
                <div class="col-md-6">
                    <label class="form-label" for="price">Giá <span class="text-danger">*</span></label>
                    <input class="form-control" type="number" step="0.01" min="0" id="price" name="price" required
                           value="<c:out value='${priceInput}'/>"/>
                </div>
                <div class="col-md-6">
                    <label class="form-label" for="categoryid">Danh mục <span class="text-danger">*</span></label>
                    <select class="form-select" id="categoryid" name="categoryid" required>
                        <option value="">-- Chọn danh mục --</option>
                        <c:forEach var="cate" items="${categories}">
                            <option value="${cate.categoryid}"
                                ${product.category.categoryid == cate.categoryid ? 'selected' : ''}>
                                <c:out value="${cate.categoryname}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="mb-3 mt-3">
                <label class="form-label" for="images">Ảnh sản phẩm</label>
                <input class="form-control" type="file" id="images" name="images"
                       accept="image/png,image/jpeg,image/gif,image/bmp"/>
                <div class="form-text">PNG, JPEG, GIF hoặc BMP; tối đa 5 MB.</div>
            </div>
            <div class="mb-4">
                <label class="form-label d-block">Trạng thái</label>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" id="status1" name="status" value="1"
                           ${empty product || product.status == 1 ? 'checked' : ''}/>
                    <label class="form-check-label" for="status1">Hoạt động</label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" id="status0" name="status" value="0"
                           ${not empty product && product.status == 0 ? 'checked' : ''}/>
                    <label class="form-check-label" for="status0">Khóa</label>
                </div>
            </div>
            <button class="btn btn-primary" type="submit">Lưu sản phẩm</button>
        </form>
    </div>
</section>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
