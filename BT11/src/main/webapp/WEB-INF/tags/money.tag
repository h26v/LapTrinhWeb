<%@ tag pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="value" required="false" type="java.math.BigDecimal" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%-- Hien thi tien: $1,234.50 (co dinh kieu en-US de khong phu thuoc ngon ngu trinh duyet) --%>
<fmt:setLocale value="en_US"/>
$<fmt:formatNumber value="${empty value ? 0 : value}" pattern="#,##0.00"/>
