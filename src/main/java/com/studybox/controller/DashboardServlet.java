package com.studybox.controller;

import com.studybox.model.User;
import com.studybox.service.StudySetService;
import com.studybox.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final StudySetService studySetService = new StudySetService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User currentUser = SessionUtil.getCurrentUser(request);
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        long userId = currentUser.getId();
        request.setAttribute("totalStudySets", studySetService.countOwnedSets(userId));
        request.setAttribute("activeStudySets", studySetService.countActiveOwnedSets(userId));
        request.setAttribute("archivedStudySets", studySetService.countArchivedOwnedSets(userId));
        request.setAttribute("recentStudySets", studySetService.getRecentOwnedStudySets(userId, 4));
        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}
