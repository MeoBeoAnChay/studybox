package com.studybox.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class FlashUtil {

    private static final String SUCCESS_KEY = "flashSuccess";
    private static final String ERROR_KEY = "flashError";

    private FlashUtil() {
    }

    public static void success(HttpSession session, String message) {
        if (session != null) {
            session.setAttribute(SUCCESS_KEY, message);
        }
    }

    public static void error(HttpSession session, String message) {
        if (session != null) {
            session.setAttribute(ERROR_KEY, message);
        }
    }

    public static void transfer(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }

        copy(session, request, SUCCESS_KEY);
        copy(session, request, ERROR_KEY);
    }

    private static void copy(HttpSession session, HttpServletRequest request, String key) {
        Object value = session.getAttribute(key);
        if (value != null) {
            request.setAttribute(key, value);
            session.removeAttribute(key);
        }
    }
}
