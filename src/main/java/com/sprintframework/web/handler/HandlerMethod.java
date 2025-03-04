package com.sprintframework.web.handler;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class HandlerMethod {
    private final Class<?> controllerClass;
    private final Method method;
    private final Parameter[] parameters;

    public HandlerMethod(Class<?> controllerClass, Method method) {
        this.controllerClass = controllerClass;
        this.method = method;
        this.parameters = method.getParameters();
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getMethod() {
        return method;
    }

    public Parameter[] getParameters() {
        return parameters;
    }
}
