<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ include file="/WEB-INF/views/fragments/taglibs.jspf" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lỗi | StudyBox</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page-shell auth-shell">
<div class="auth-card" style="text-align: center;">
    <div class="brand" style="justify-content: center;">
        <div class="brand-mark">S</div>
        <span>StudyBox</span>
    </div>

    <div class="hero-copy">
        <h1>Đã có lỗi xảy ra</h1>
        <p>Trang hoặc thao tác bạn yêu cầu hiện không thể hoàn tất.</p>
    </div>

    <div class="flash flash-error">
        <strong>
            <c:choose>
                <c:when test="${not empty requestScope['javax.servlet.error.status_code']}">
                    Mã lỗi: <c:out value="${requestScope['javax.servlet.error.status_code']}"/>
                </c:when>
                <c:otherwise>Lỗi hệ thống</c:otherwise>
            </c:choose>
        </strong>
        <div style="margin-top: 8px;">
            <c:choose>
                <c:when test="${not empty exception and not empty exception.message}">
                    <c:out value="${exception.message}"/>
                </c:when>
                <c:when test="${not empty requestScope['javax.servlet.error.message']}">
                    <c:out value="${requestScope['javax.servlet.error.message']}"/>
                </c:when>
                <c:otherwise>Vui lòng thử lại sau.</c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="help-row" style="justify-content: center;">
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/dashboard">Về dashboard</a>
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/login">Đăng nhập</a>
    </div>
</div>
</body>
</html>
