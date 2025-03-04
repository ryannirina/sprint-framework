package com.sprintframework.web.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class Session {
    private final HttpSession httpSession;

    public Session(HttpServletRequest request) {
        this.httpSession = request.getSession();
    }

    public void setAttribute(String name, Object value) {
        httpSession.setAttribute(name, value);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getAttribute(String name) {
        Object value = httpSession.getAttribute(name);
        return Optional.ofNullable((T) value);
    }

    public void removeAttribute(String name) {
        httpSession.removeAttribute(name);
    }

    public void invalidate() {
        httpSession.invalidate();
    }

    public String getId() {
        return httpSession.getId();
    }

    public boolean isNew() {
        return httpSession.isNew();
    }

    public void setMaxInactiveInterval(int interval) {
        httpSession.setMaxInactiveInterval(interval);
    }

    public int getMaxInactiveInterval() {
        return httpSession.getMaxInactiveInterval();
    }
}
