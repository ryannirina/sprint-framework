package com.sprintframework.web.handler;

import com.sprintframework.web.annotation.GetMapping;
import com.sprintframework.web.exception.SprintFrameworkException;
import com.sprintframework.web.exception.SprintFrameworkException.ErrorType;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {
    private final Map<String, HandlerMethod> handlers = new HashMap<>();

    public void registerController(Class<?> controllerClass) {
        Method[] methods = controllerClass.getDeclaredMethods();
        for (Method method : methods) {
            GetMapping mapping = method.getAnnotation(GetMapping.class);
            if (mapping != null) {
                String url = mapping.value();
                if (handlers.containsKey(url)) {
                    HandlerMethod existing = handlers.get(url);
                    throw new SprintFrameworkException(
                        ErrorType.DUPLICATE_URL,
                        String.format(
                            "Duplicate URL mapping '%s' found in %s and %s",
                            url,
                            existing.getControllerClass().getName() + "." + existing.getMethod().getName(),
                            controllerClass.getName() + "." + method.getName()
                        )
                    );
                }
                handlers.put(url, new HandlerMethod(controllerClass, method));
            }
        }
    }

    public HandlerMethod getHandler(String url) {
        HandlerMethod handler = handlers.get(url);
        if (handler == null) {
            throw new SprintFrameworkException(
                ErrorType.URL_NOT_FOUND,
                "No handler found for URL: " + url
            );
        }
        return handler;
    }
}
