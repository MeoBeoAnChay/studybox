package com.studybox.controller;

import com.studybox.exception.NotFoundException;
import com.studybox.exception.ValidationException;
import com.studybox.model.StudySet;
import com.studybox.model.User;
import com.studybox.service.StudySetService;
import com.studybox.util.FlashUtil;
import com.studybox.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/study-sets/*")
public class StudySetServlet extends HttpServlet {

    private final StudySetService studySetService = new StudySetService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User currentUser = requireCurrentUser(request, response);
        if (currentUser == null) {
            return;
        }

        try {
            String path = normalizePath(request.getPathInfo());
            if (path.isEmpty()) {
                listStudySets(request, response, currentUser);
                return;
            }

            switch (path) {
                case "/new":
                    showCreateForm(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response, currentUser);
                    break;
                case "/view":
                    showDetail(request, response, currentUser);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (NotFoundException ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User currentUser = requireCurrentUser(request, response);
        if (currentUser == null) {
            return;
        }

        try {
            String path = normalizePath(request.getPathInfo());
            switch (path) {
                case "/new":
                    createStudySet(request, response, currentUser);
                    break;
                case "/edit":
                    updateStudySet(request, response, currentUser);
                    break;
                case "/delete":
                    deleteStudySet(request, response, currentUser);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (NotFoundException ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, ex.getMessage());
        }
    }

    private void listStudySets(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException {
        List<StudySet> studySets = studySetService.getOwnedStudySets(currentUser.getId());
        request.setAttribute("studySets", studySets);
        request.getRequestDispatcher("/WEB-INF/views/study-sets/list.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("studySet", new StudySet());
        request.setAttribute("formMode", "create");
        request.getRequestDispatcher("/WEB-INF/views/study-sets/form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException {
        long studySetId = parseId(request.getParameter("id"));
        StudySet studySet = studySetService.getOwnedStudySet(currentUser.getId(), studySetId);
        request.setAttribute("studySet", studySet);
        request.setAttribute("formMode", "edit");
        request.getRequestDispatcher("/WEB-INF/views/study-sets/form.jsp").forward(request, response);
    }

    private void showDetail(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException {
        long studySetId = parseId(request.getParameter("id"));
        StudySet studySet = studySetService.getOwnedStudySet(currentUser.getId(), studySetId);
        request.setAttribute("studySet", studySet);
        request.getRequestDispatcher("/WEB-INF/views/study-sets/detail.jsp").forward(request, response);
    }

    private void createStudySet(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException {
        StudySet form = readStudySetForm(request);
        try {
            StudySet created = studySetService.createStudySet(currentUser.getId(), form);
            FlashUtil.success(request.getSession(), "Đã tạo Study Set \"" + created.getName() + "\".");
            response.sendRedirect(request.getContextPath() + "/study-sets");
        } catch (ValidationException ex) {
            request.setAttribute("studySet", form);
            request.setAttribute("formMode", "create");
            request.setAttribute("errors", ex.getErrors());
            request.getRequestDispatcher("/WEB-INF/views/study-sets/form.jsp").forward(request, response);
        }
    }

    private void updateStudySet(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException {
        long studySetId = parseId(request.getParameter("id"));
        StudySet form = readStudySetForm(request);
        try {
            StudySet updated = studySetService.updateStudySet(currentUser.getId(), studySetId, form);
            FlashUtil.success(request.getSession(), "Đã cập nhật Study Set \"" + updated.getName() + "\".");
            response.sendRedirect(request.getContextPath() + "/study-sets/view?id=" + updated.getId());
        } catch (ValidationException ex) {
            form.setId(studySetId);
            request.setAttribute("studySet", form);
            request.setAttribute("formMode", "edit");
            request.setAttribute("errors", ex.getErrors());
            request.getRequestDispatcher("/WEB-INF/views/study-sets/form.jsp").forward(request, response);
        } catch (NotFoundException ex) {
            FlashUtil.error(request.getSession(), ex.getMessage());
            response.sendRedirect(request.getContextPath() + "/study-sets");
        }
    }

    private void deleteStudySet(HttpServletRequest request, HttpServletResponse response, User currentUser) throws IOException {
        long studySetId = parseId(request.getParameter("id"));
        try {
            studySetService.deleteStudySet(currentUser.getId(), studySetId);
            FlashUtil.success(request.getSession(), "Đã xóa Study Set.");
        } catch (NotFoundException ex) {
            FlashUtil.error(request.getSession(), ex.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/study-sets");
    }

    private StudySet readStudySetForm(HttpServletRequest request) {
        StudySet studySet = new StudySet();
        studySet.setName(request.getParameter("name"));
        studySet.setDescription(request.getParameter("description"));
        studySet.setSubject(request.getParameter("subject"));
        studySet.setStatus(request.getParameter("status"));
        return studySet;
    }

    private long parseId(String value) {
        try {
            return Long.parseLong(value);
        } catch (Exception ex) {
            throw new NotFoundException("ID Study Set không hợp lệ.");
        }
    }

    private String normalizePath(String pathInfo) {
        if (pathInfo == null || "/".equals(pathInfo)) {
            return "";
        }
        return pathInfo;
    }

    private User requireCurrentUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User currentUser = SessionUtil.getCurrentUser(request);
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
        }
        return currentUser;
    }
}
