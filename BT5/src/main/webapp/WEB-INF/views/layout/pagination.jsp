<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%-- Phan trang dung chung. Can: currentPage, totalPages, keyword, baseUrl. --%>
<c:if test="${totalPages > 1}">
    <nav aria-label="Phân trang">
        <ul class="pagination justify-content-center mb-0">
            <li class="page-item ${currentPage <= 1 ? 'disabled' : ''}">
                <c:url var="prevUrl" value="${baseUrl}">
                    <c:param name="page" value="${currentPage - 1}"/>
                    <c:if test="${not empty keyword}"><c:param name="keyword" value="${keyword}"/></c:if>
                </c:url>
                <a class="page-link" href="${prevUrl}">Trước</a>
            </li>

            <c:forEach var="i" begin="1" end="${totalPages}">
                <c:url var="pageUrl" value="${baseUrl}">
                    <c:param name="page" value="${i}"/>
                    <c:if test="${not empty keyword}"><c:param name="keyword" value="${keyword}"/></c:if>
                </c:url>
                <li class="page-item ${i == currentPage ? 'active' : ''}">
                    <a class="page-link" href="${pageUrl}"><c:out value="${i}"/></a>
                </li>
            </c:forEach>

            <li class="page-item ${currentPage >= totalPages ? 'disabled' : ''}">
                <c:url var="nextUrl" value="${baseUrl}">
                    <c:param name="page" value="${currentPage + 1}"/>
                    <c:if test="${not empty keyword}"><c:param name="keyword" value="${keyword}"/></c:if>
                </c:url>
                <a class="page-link" href="${nextUrl}">Sau</a>
            </li>
        </ul>
    </nav>
</c:if>
