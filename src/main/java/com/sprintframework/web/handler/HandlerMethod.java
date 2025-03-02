package com.sprintframework.web.handler;

import java.lang.reflect.Method;

public class HandlerMethod {
    private final Class<?> controllerClass;
    private final Method method;

    public HandlerMethod(Class<?> controllerClass, Method method) {
        this.controllerClass = controllerClass;
        this.method = method;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getMethod() {
        return method;
    }
}
