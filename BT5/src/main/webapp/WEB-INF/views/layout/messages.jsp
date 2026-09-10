<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%-- Thong bao flash sau khi redirect (them/sua/xoa thanh cong hoac that bai). --%>
<c:if test="${not empty success}">
    <div class="alert alert-success" role="alert"><c:out value="${success}"/></div>
</c:if>
<c:if test="${not empty error}">
    <div class="alert alert-danger" role="alert"><c:out value="${error}"/></div>
</c:if>
<%-- Loi validate khi tra ve truc tiep form (khong qua redirect). --%>
<c:if test="${not empty alert}">
    <div class="alert alert-danger" role="alert"><c:out value="${alert}"/></div>
</c:if>
