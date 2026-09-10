<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Danh sách sản phẩm" scope="request"/>
<c:set var="adminTab" value="product" scope="request"/>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>

<section class="card border-0">
    <div class="card-body p-4">
        <jsp:include page="/WEB-INF/views/layout/admin-tabs.jsp"/>

        <div class="d-flex justify-content-between align-items-center gap-3 flex-wrap mb-3">
            <div>
                <h1 class="h3 mb-1">Danh sách sản phẩm</h1>
                <p class="text-muted mb-0">Tổng số: <c:out value="${total}"/></p>
            </div>
            <a class="btn btn-primary" href="<c:url value='/admin/product/add'/>">Thêm sản phẩm</a>
        </div>

        <jsp:include page="/WEB-INF/views/layout/messages.jsp"/>

        <form class="row g-2 mb-3" action="<c:url value='/admin/products'/>" method="get">
            <div class="col-sm-8 col-md-5">
                <input class="form-control" type="text" name="keyword" maxlength="100"
                       value="<c:out value='${keyword}'/>" placeholder="Tìm theo tên sản phẩm..."/>
            </div>
            <div class="col-auto">
                <button class="btn btn-outline-primary" type="submit">Tìm</button>
            </div>
            <c:if test="${not empty keyword}">
                <div class="col-auto">
                    <a class="btn btn-outline-secondary" href="<c:url value='/admin/products'/>">Xóa lọc</a>
                </div>
            </c:if>
        </form>

        <div class="table-responsive">
            <table class="table table-bordered table-hover align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th style="width:70px">ID</th>
                        <th style="width:100px">Ảnh</th>
                        <th>Tên sản phẩm</th>
                        <th>Danh mục</th>
                        <th style="width:140px">Giá</th>
                        <th style="width:120px">Trạng thái</th>
                        <th style="width:150px">Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${products}">
                        <c:url var="editUrl" value="/admin/product/edit">
                            <c:param name="id" value="${p.productId}"/>
                        </c:url>
                        <c:url var="deleteUrl" value="/admin/product/delete">
                            <c:param name="id" value="${p.productId}"/>
                        </c:url>
                        <tr>
                            <td><c:out value="${p.productId}"/></td>
                            <td>
                                <c:if test="${not empty p.images}">
                                    <c:choose>
                                        <c:when test="${fn:startsWith(p.images, 'https://') || fn:startsWith(p.images, 'http://')}">
                                            <img class="icon" src="<c:out value='${p.images}'/>" alt="Ảnh sản phẩm"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:url var="imageUrl" value="/image">
                                                <c:param name="fname" value="${p.images}"/>
                                            </c:url>
                                            <img class="icon" src="${imageUrl}" alt="Ảnh sản phẩm"/>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </td>
                            <td><c:out value="${p.productName}"/></td>
                            <td><c:out value="${p.category.categoryname}"/></td>
                            <td class="text-danger fw-bold">
                                <fmt:formatNumber value="${p.price}" type="number" groupingUsed="true"/> đ
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${p.status == 1}">
                                        <span class="badge text-bg-success">Hoạt động</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge text-bg-secondary">Khóa</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a class="btn btn-sm btn-outline-primary" href="${editUrl}">Sửa</a>
                                <a class="btn btn-sm btn-outline-danger" href="${deleteUrl}"
                                   onclick="return confirm('Xóa sản phẩm này?');">Xóa</a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty products}">
                        <tr>
                            <td colspan="7" class="text-center text-muted">
                                <c:choose>
                                    <c:when test="${not empty keyword}">Không tìm thấy sản phẩm nào phù hợp.</c:when>
                                    <c:otherwise>Chưa có sản phẩm nào.</c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</section>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
