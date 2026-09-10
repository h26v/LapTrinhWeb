<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="Sửa danh mục" scope="request"/>
<c:set var="adminTab" value="category" scope="request"/>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>

<section class="card border-0 mx-auto" style="max-width: 680px;">
    <div class="card-body p-4">
        <div class="d-flex justify-content-between align-items-center gap-3 flex-wrap mb-3">
            <h1 class="h3 mb-0">Sửa danh mục #<c:out value="${category.categoryid}"/></h1>
            <a class="btn btn-outline-secondary" href="<c:url value='/admin/categories'/>">Quay lại</a>
        </div>

        <jsp:include page="/WEB-INF/views/layout/messages.jsp"/>

        <form action="<c:url value='/admin/category/update'/>" method="post" enctype="multipart/form-data">
            <input type="hidden" name="categoryid" value="<c:out value='${category.categoryid}'/>"/>

            <div class="mb-3">
                <label class="form-label" for="categoryname">Tên danh mục</label>
                <input class="form-control" type="text" id="categoryname" name="categoryname" required maxlength="255"
                       value="<c:out value='${category.categoryname}'/>"/>
            </div>

            <div class="mb-3">
                <label class="form-label">Ảnh hiện tại</label>
                <div>
                    <c:choose>
                        <c:when test="${empty category.images}">
                            <span class="text-muted">Chưa có ảnh.</span>
                        </c:when>
                        <c:when test="${fn:startsWith(category.images, 'https://') || fn:startsWith(category.images, 'http://')}">
                            <img class="icon" src="<c:out value='${category.images}'/>" alt="Ảnh danh mục"/>
                        </c:when>
                        <c:otherwise>
                            <c:url var="imageUrl" value="/image">
                                <c:param name="fname" value="${category.images}"/>
                            </c:url>
                            <img class="icon" src="${imageUrl}" alt="Ảnh danh mục"/>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label" for="images1">Đổi ảnh khác</label>
                <input class="form-control" type="file" id="images1" name="images1"
                       accept="image/png,image/jpeg,image/gif,image/bmp"/>
                <div class="form-text">Bỏ trống để giữ ảnh hiện tại. PNG, JPEG, GIF hoặc BMP; tối đa 5 MB.</div>
            </div>

            <div class="mb-4">
                <label class="form-label d-block">Trạng thái</label>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" id="status1" name="status" value="1"
                           ${category.status == 1 ? 'checked' : ''}/>
                    <label class="form-check-label" for="status1">Hoạt động</label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" id="status0" name="status" value="0"
                           ${category.status == 0 ? 'checked' : ''}/>
                    <label class="form-check-label" for="status0">Khóa</label>
                </div>
            </div>

            <button class="btn btn-primary" type="submit">Cập nhật</button>
        </form>
    </div>
</section>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
