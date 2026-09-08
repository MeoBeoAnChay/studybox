<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/fragments/taglibs.jspf" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết Study Set | StudyBox</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="app-shell">
    <aside class="sidebar">
        <div class="brand">
            <div class="brand-mark">S</div>
            <span>StudyBox</span>
        </div>

        <nav class="sidebar-nav">
            <a class="nav-item" href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
            <a class="nav-item active" href="${pageContext.request.contextPath}/study-sets">Study Sets</a>
            <a class="nav-item" href="${pageContext.request.contextPath}/study-sets/new">Tạo bộ mới</a>
        </nav>

        <div class="sidebar-footer">
            <div style="font-weight: 700;"><c:out value="${sessionScope.currentUser.fullName}"/></div>
            <div class="subtle" style="color: #cbd5e1;"><c:out value="${sessionScope.currentUser.email}"/></div>
            <div style="margin-top: 12px;">
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
            </div>
        </div>
    </aside>

    <main class="content">
        <div class="topbar">
            <div>
                <h1><c:out value="${studySet.name}"/></h1>
                <p class="subtle">Chủ đề: <c:out value="${studySet.subject}"/></p>
            </div>
            <div class="page-actions">
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/study-sets/edit?id=${studySet.id}">Sửa</a>
                <form method="post" action="${pageContext.request.contextPath}/study-sets/delete" style="display: inline;">
                    <input type="hidden" name="id" value="${studySet.id}">
                    <button class="btn btn-danger" type="submit" data-confirm="Bạn có chắc muốn xóa Study Set này không?">Xóa</button>
                </form>
            </div>
        </div>

        <%@ include file="/WEB-INF/views/fragments/messages.jspf" %>

        <section class="grid-cards">
            <article class="card stat-card">
                <div class="stat-label">Trạng thái</div>
                <div class="stat-value">
                    <c:choose>
                        <c:when test="${studySet.status eq 'ACTIVE'}">Active</c:when>
                        <c:otherwise>Archived</c:otherwise>
                    </c:choose>
                </div>
            </article>
            <article class="card stat-card">
                <div class="stat-label">Tạo lúc</div>
                <div class="stat-value" style="font-size: 1.15rem;">
                    <fmt:formatDate value="${studySet.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                </div>
            </article>
            <article class="card stat-card">
                <div class="stat-label">Cập nhật</div>
                <div class="stat-value" style="font-size: 1.15rem;">
                    <fmt:formatDate value="${studySet.updatedAt}" pattern="dd/MM/yyyy HH:mm"/>
                </div>
            </article>
            <article class="card stat-card">
                <div class="stat-label">Tác vụ nhanh</div>
                <div class="stat-value" style="font-size: 1.15rem;">CRUD</div>
            </article>
        </section>

        <section class="section detail-grid">
            <article class="card">
                <h2 class="section-title">Mô tả</h2>
                <p class="subtle" style="margin-top: 12px; white-space: pre-wrap;">
                    <c:choose>
                        <c:when test="${empty studySet.description}">
                            Chưa có mô tả.
                        </c:when>
                        <c:otherwise>
                            <c:out value="${studySet.description}"/>
                        </c:otherwise>
                    </c:choose>
                </p>
            </article>

            <article class="card">
                <h2 class="section-title">Thông tin nhanh</h2>
                <div class="meta-list" style="margin-top: 12px;">
                    <div class="meta-item">
                        <span>Tên bộ</span>
                        <strong><c:out value="${studySet.name}"/></strong>
                    </div>
                    <div class="meta-item">
                        <span>Chủ đề</span>
                        <strong><c:out value="${studySet.subject}"/></strong>
                    </div>
                    <div class="meta-item">
                        <span>Trạng thái</span>
                        <strong><c:out value="${studySet.status}"/></strong>
                    </div>
                </div>
            </article>
        </section>

        <section class="section card">
            <div style="display: flex; justify-content: space-between; align-items: center; gap: 12px; margin-bottom: 16px;">
                <h2 class="section-title">Hành động</h2>
                <a class="btn-link" href="${pageContext.request.contextPath}/study-sets">Quay lại danh sách</a>
            </div>
            <div class="actions">
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/study-sets/edit?id=${studySet.id}">Chỉnh sửa</a>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/study-sets">Xem tất cả</a>
            </div>
        </section>
    </main>
</div>
<script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
