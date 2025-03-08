package com.sprintframework.web.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ControllerRegistry {
    private static final ControllerRegistry INSTANCE = new ControllerRegistry();
    private final Map<Class<?>, Object> controllers = new ConcurrentHashMap<>();

    private ControllerRegistry() {}

    public static ControllerRegistry getInstance() {
        return INSTANCE;
    }

    public Object getController(Class<?> controllerClass) {
        return controllers.computeIfAbsent(controllerClass, clazz -> {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Failed to create controller instance: " + clazz.getName(), e);
            }
        });
    }

    public void clear() {
        controllers.clear();
    }
}
