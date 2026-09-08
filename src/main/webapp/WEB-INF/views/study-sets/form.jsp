<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/fragments/taglibs.jspf" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
        <c:choose>
            <c:when test="${formMode eq 'edit'}">Sửa Study Set</c:when>
            <c:otherwise>Tạo Study Set</c:otherwise>
        </c:choose>
        | StudyBox
    </title>
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
                <h1>
                    <c:choose>
                        <c:when test="${formMode eq 'edit'}">Sửa Study Set</c:when>
                        <c:otherwise>Tạo Study Set</c:otherwise>
                    </c:choose>
                </h1>
                <p class="subtle">Điền thông tin cơ bản để bắt đầu quản lý bộ học tập.</p>
            </div>
            <div class="page-actions">
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/study-sets">Quay lại danh sách</a>
            </div>
        </div>

        <%@ include file="/WEB-INF/views/fragments/messages.jspf" %>

        <section class="card">
            <c:set var="statusValue" value="${empty studySet.status ? 'ACTIVE' : studySet.status}" />
            <c:set var="formAction" value="${pageContext.request.contextPath}/study-sets/new" />
            <c:if test="${formMode eq 'edit'}">
                <c:set var="formAction" value="${pageContext.request.contextPath}/study-sets/edit" />
            </c:if>

            <form method="post" action="${formAction}">
                <c:if test="${formMode eq 'edit'}">
                    <input type="hidden" name="id" value="${studySet.id}">
                </c:if>

                <div class="field">
                    <label for="name">Tên bộ học tập</label>
                    <input class="input" type="text" id="name" name="name" maxlength="150"
                           value="${fn:escapeXml(studySet.name)}" placeholder="Java Core" required>
                </div>

                <div class="field">
                    <label for="subject">Chủ đề</label>
                    <input class="input" type="text" id="subject" name="subject" maxlength="100"
                           value="${fn:escapeXml(studySet.subject)}" placeholder="Java, SQL, IELTS..." required>
                </div>

                <div class="field">
                    <label for="description">Mô tả</label>
                    <textarea class="textarea" id="description" name="description"
                              placeholder="Mô tả ngắn về bộ học tập..."><c:out value='${studySet.description}'/></textarea>
                </div>

                <div class="field">
                    <label for="status">Trạng thái</label>
                    <select class="select" id="status" name="status">
                        <option value="ACTIVE" ${statusValue eq 'ACTIVE' ? 'selected' : ''}>Active</option>
                        <option value="ARCHIVED" ${statusValue eq 'ARCHIVED' ? 'selected' : ''}>Archived</option>
                    </select>
                </div>

                <div class="actions" style="justify-content: flex-end; margin-top: 22px;">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/study-sets">Hủy</a>
                    <button class="btn btn-primary" type="submit">
                        <c:choose>
                            <c:when test="${formMode eq 'edit'}">Cập nhật</c:when>
                            <c:otherwise>Tạo mới</c:otherwise>
                        </c:choose>
                    </button>
                </div>
            </form>
        </section>
    </main>
</div>
<script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
