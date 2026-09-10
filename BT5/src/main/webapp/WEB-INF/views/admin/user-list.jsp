<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Danh sách người dùng" scope="request"/>
<c:set var="adminTab" value="user" scope="request"/>
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>

<section class="card border-0">
    <div class="card-body p-4">
        <jsp:include page="/WEB-INF/views/layout/admin-tabs.jsp"/>

        <div class="d-flex justify-content-between align-items-center gap-3 flex-wrap mb-3">
            <div>
                <h1 class="h3 mb-1">Danh sách người dùng</h1>
                <p class="text-muted mb-0">
                    Tổng số: <c:out value="${total}"/>
                    <c:if test="${not empty keyword}">
                        · Tìm thấy <strong><c:out value="${resultSize}"/></strong> kết quả cho
                        “<c:out value="${keyword}"/>”
                    </c:if>
                </p>
            </div>
            <a class="btn btn-primary" href="<c:url value='/admin/user/add'/>">Thêm người dùng</a>
        </div>

        <jsp:include page="/WEB-INF/views/layout/messages.jsp"/>

        <%-- Form tim kiem theo username / ho ten / email / dien thoai --%>
        <form class="row g-2 mb-3" action="<c:url value='/admin/users'/>" method="get">
            <div class="col-sm-8 col-md-5">
                <input class="form-control" type="text" name="keyword" maxlength="100"
                       value="<c:out value='${keyword}'/>"
                       placeholder="Tìm theo tên đăng nhập, họ tên, email, điện thoại..."/>
            </div>
            <div class="col-auto">
                <button class="btn btn-outline-primary" type="submit">Tìm</button>
            </div>
            <c:if test="${not empty keyword}">
                <div class="col-auto">
                    <a class="btn btn-outline-secondary" href="<c:url value='/admin/users'/>">Xóa lọc</a>
                </div>
            </c:if>
        </form>

        <div class="table-responsive">
            <table class="table table-bordered table-hover align-middle mb-3">
                <thead class="table-light">
                    <tr>
                        <th style="width:70px">ID</th>
                        <th style="width:70px">Ảnh</th>
                        <th>Tên đăng nhập</th>
                        <th>Họ và tên</th>
                        <th>Email</th>
                        <th style="width:130px">Điện thoại</th>
                        <th style="width:110px">Vai trò</th>
                        <th style="width:120px">Trạng thái</th>
                        <th style="width:150px">Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="u" items="${users}">
                        <c:url var="editUrl" value="/admin/user/edit">
                            <c:param name="id" value="${u.id}"/>
                        </c:url>
                        <c:url var="deleteUrl" value="/admin/user/delete">
                            <c:param name="id" value="${u.id}"/>
                        </c:url>
                        <tr>
                            <td><c:out value="${u.id}"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${empty u.avatar}">
                                        <div class="avatar-empty-sm">N/A</div>
                                    </c:when>
                                    <c:when test="${fn:startsWith(u.avatar, 'https://') || fn:startsWith(u.avatar, 'http://')}">
                                        <img class="avatar-sm" src="<c:out value='${u.avatar}'/>" alt="Ảnh đại diện"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:url var="avatarUrl" value="/image">
                                            <c:param name="fname" value="${u.avatar}"/>
                                        </c:url>
                                        <img class="avatar-sm" src="${avatarUrl}" alt="Ảnh đại diện"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><c:out value="${u.userName}"/></td>
                            <td><c:out value="${u.fullName}"/></td>
                            <td><c:out value="${u.email}"/></td>
                            <td><c:out value="${u.phone}"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${u.roleid == 1}">
                                        <span class="badge text-bg-primary">Admin</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge text-bg-light border">Người dùng</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${u.active == 1}">
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
                                   onclick="return confirm('Xóa người dùng này?');">Xóa</a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty users}">
                        <tr>
                            <td colspan="9" class="text-center text-muted">
                                <c:choose>
                                    <c:when test="${not empty keyword}">Không tìm thấy người dùng nào phù hợp.</c:when>
                                    <c:otherwise>Chưa có người dùng nào.</c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

        <c:set var="baseUrl" value="/admin/users" scope="request"/>
        <jsp:include page="/WEB-INF/views/layout/pagination.jsp"/>
    </div>
</section>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
