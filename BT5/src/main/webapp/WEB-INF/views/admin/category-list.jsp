<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="Danh sách danh mục" scope="request"/>
<c:set var="adminTab" value="category" scope="request"/>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>

<section class="card border-0">
    <div class="card-body p-4">
        <jsp:include page="/WEB-INF/views/layout/admin-tabs.jsp"/>

        <div class="d-flex justify-content-between align-items-center gap-3 flex-wrap mb-3">
            <div>
                <h1 class="h3 mb-1">Danh sách danh mục</h1>
                <p class="text-muted mb-0">
                    Tổng số: <c:out value="${total}"/>
                    <c:if test="${not empty keyword}">
                        · Tìm thấy <strong><c:out value="${resultSize}"/></strong> kết quả cho
                        “<c:out value="${keyword}"/>”
                    </c:if>
                </p>
            </div>
            <a class="btn btn-primary" href="<c:url value='/admin/category/add'/>">Thêm danh mục</a>
        </div>

        <jsp:include page="/WEB-INF/views/layout/messages.jsp"/>

        <%-- Form tim kiem theo ten danh muc --%>
        <form class="row g-2 mb-3" action="<c:url value='/admin/categories'/>" method="get">
            <div class="col-sm-8 col-md-5">
                <input class="form-control" type="text" name="keyword" maxlength="100"
                       value="<c:out value='${keyword}'/>" placeholder="Tìm theo tên danh mục..."/>
            </div>
            <div class="col-auto">
                <button class="btn btn-outline-primary" type="submit">Tìm</button>
            </div>
            <c:if test="${not empty keyword}">
                <div class="col-auto">
                    <a class="btn btn-outline-secondary" href="<c:url value='/admin/categories'/>">Xóa lọc</a>
                </div>
            </c:if>
        </form>

        <div class="table-responsive">
            <table class="table table-bordered table-hover align-middle mb-3">
                <thead class="table-light">
                    <tr>
                        <th style="width:70px">ID</th>
                        <th>Tên danh mục</th>
                        <th style="width:100px">Ảnh</th>
                        <th style="width:130px">Trạng thái</th>
                        <th style="width:150px">Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="cate" items="${categories}">
                        <c:url var="editUrl" value="/admin/category/edit">
                            <c:param name="id" value="${cate.categoryid}"/>
                        </c:url>
                        <c:url var="deleteUrl" value="/admin/category/delete">
                            <c:param name="id" value="${cate.categoryid}"/>
                        </c:url>
                        <tr>
                            <td><c:out value="${cate.categoryid}"/></td>
                            <td><c:out value="${cate.categoryname}"/></td>
                            <td>
                                <c:if test="${not empty cate.images}">
                                    <c:choose>
                                        <c:when test="${fn:startsWith(cate.images, 'https://') || fn:startsWith(cate.images, 'http://')}">
                                            <img class="icon" src="<c:out value='${cate.images}'/>" alt="Ảnh danh mục"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:url var="imageUrl" value="/image">
                                                <c:param name="fname" value="${cate.images}"/>
                                            </c:url>
                                            <img class="icon" src="${imageUrl}" alt="Ảnh danh mục"/>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${cate.status == 1}">
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
                                   onclick="return confirm('Xóa danh mục này?');">Xóa</a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty categories}">
                        <tr>
                            <td colspan="5" class="text-center text-muted">
                                <c:choose>
                                    <c:when test="${not empty keyword}">Không tìm thấy danh mục nào phù hợp.</c:when>
                                    <c:otherwise>Chưa có danh mục nào.</c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

        <c:set var="baseUrl" value="/admin/categories" scope="request"/>
        <jsp:include page="/WEB-INF/views/layout/pagination.jsp"/>
    </div>
</section>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
