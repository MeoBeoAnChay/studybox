package com.studybox.util;

import com.studybox.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class SessionUtil {

    public static final String CURRENT_USER_KEY = "currentUser";

    private SessionUtil() {
    }

    public static User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object user = session.getAttribute(CURRENT_USER_KEY);
        return user instanceof User ? (User) user : null;
    }

    public static void setCurrentUser(HttpSession session, User user) {
        session.setAttribute(CURRENT_USER_KEY, user);
    }

    public static void clearCurrentUser(HttpSession session) {
        if (session != null) {
            session.removeAttribute(CURRENT_USER_KEY);
        }
    }
}
