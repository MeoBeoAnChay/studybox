<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/fragments/taglibs.jspf" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard | StudyBox</title>
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
            <a class="nav-item active" href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
            <a class="nav-item" href="${pageContext.request.contextPath}/study-sets">Study Sets</a>
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
                <h1>Xin chào, <c:out value="${sessionScope.currentUser.fullName}"/>!</h1>
                <p class="subtle">Đây là tổng quan nhanh để bạn tiếp tục tự học mà không bị phân tâm.</p>
            </div>
            <div class="page-actions">
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/study-sets/new">+ Tạo Study Set</a>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/study-sets">Xem danh sách</a>
            </div>
        </div>

        <%@ include file="/WEB-INF/views/fragments/messages.jspf" %>

        <section class="grid-cards">
            <article class="card stat-card">
                <div class="stat-label">Tổng bộ học tập</div>
                <div class="stat-value">${totalStudySets}</div>
            </article>
            <article class="card stat-card">
                <div class="stat-label">Đang hoạt động</div>
                <div class="stat-value">${activeStudySets}</div>
            </article>
            <article class="card stat-card">
                <div class="stat-label">Đã lưu trữ</div>
                <div class="stat-value">${archivedStudySets}</div>
            </article>
            <article class="card stat-card">
                <div class="stat-label">Lối tắt</div>
                <div class="stat-value">Phase 1</div>
            </article>
        </section>

        <section class="section card">
            <div style="display: flex; justify-content: space-between; gap: 12px; align-items: center; margin-bottom: 16px;">
                <div>
                    <h2 class="section-title">Bộ học tập gần đây</h2>
                    <p class="subtle" style="margin-top: 6px;">Mở lại bộ bạn vừa làm việc gần nhất.</p>
                </div>
                <a class="btn-link" href="${pageContext.request.contextPath}/study-sets">Xem tất cả</a>
            </div>

            <c:choose>
                <c:when test="${empty recentStudySets}">
                    <div class="empty-state">
                        Bạn chưa tạo Study Set nào. Hãy tạo bộ đầu tiên để bắt đầu.
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table-wrap">
                        <table>
                            <thead>
                            <tr>
                                <th>Tên bộ</th>
                                <th>Chủ đề</th>
                                <th>Trạng thái</th>
                                <th>Cập nhật</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${recentStudySets}" var="item">
                                <tr>
                                    <td>
                                        <div style="font-weight: 700;"><c:out value="${item.name}"/></div>
                                        <div class="subtle"><c:out value="${item.description}"/></div>
                                    </td>
                                    <td><c:out value="${item.subject}"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${item.status eq 'ACTIVE'}">
                                                <span class="badge badge-success">Active</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-neutral">Archived</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${item.updatedAt}" pattern="dd/MM/yyyy HH:mm"/>
                                    </td>
                                    <td>
                                        <a class="btn-link" href="${pageContext.request.contextPath}/study-sets/view?id=${item.id}">Chi tiết</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>
    </main>
</div>
</body>
</html>
