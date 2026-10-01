<%@ tag pageEncoding="UTF-8" body-content="empty" %>
<%@ attribute name="src" required="false" %>
<%@ attribute name="alt" required="false" %>
<%@ attribute name="cssClass" required="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%-- Hien thi anh bia: link http(s) dung truc tiep, ten file upload thi lay qua /image --%>
<c:choose>
    <c:when test="${empty src}">
        <c:set var="coverUrl" value="https://placehold.co/300x400?text=No+Cover"/>
    </c:when>
    <c:when test="${fn:startsWith(src, 'http://') or fn:startsWith(src, 'https://')}">
        <c:set var="coverUrl" value="${src}"/>
    </c:when>
    <c:otherwise>
        <c:url var="coverUrl" value="/image"><c:param name="fname" value="${src}"/></c:url>
    </c:otherwise>
</c:choose>
<img src="${fn:escapeXml(coverUrl)}" alt="${fn:escapeXml(alt)}" class="${cssClass}">
