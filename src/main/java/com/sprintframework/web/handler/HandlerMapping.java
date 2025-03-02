package com.sprintframework.web.handler;

import com.sprintframework.web.annotation.GetMapping;
import com.sprintframework.web.exception.URLMappingException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {
    private final Map<String, HandlerMethod> urlMap = new HashMap<>();

    public void registerController(Class<?> controllerClass) {
        Method[] methods = controllerClass.getDeclaredMethods();
        for (Method method : methods) {
            GetMapping mapping = method.getAnnotation(GetMapping.class);
            if (mapping != null) {
                String url = mapping.value();
                if (urlMap.containsKey(url)) {
                    throw new URLMappingException("URL '" + url + "' is already mapped to " + 
                        urlMap.get(url).getMethod().getName() + " in " + 
                        urlMap.get(url).getControllerClass().getName());
                }
                urlMap.put(url, new HandlerMethod(controllerClass, method));
            }
        }
    }

    public HandlerMethod getHandler(String url) {
        return urlMap.get(url);
    }
}
