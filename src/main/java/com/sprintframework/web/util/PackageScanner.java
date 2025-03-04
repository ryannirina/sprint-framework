package com.sprintframework.web.util;

import com.sprintframework.web.annotation.Controller;
import com.sprintframework.web.exception.SprintFrameworkException;
import com.sprintframework.web.exception.SprintFrameworkException.ErrorType;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PackageScanner {
    public static List<Class<?>> findControllers(String basePackage) {
        try {
            String path = basePackage.replace('.', '/');
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = classLoader.getResources(path);
            
            if (!resources.hasMoreElements()) {
                throw new SprintFrameworkException(
                    ErrorType.PACKAGE_NOT_FOUND,
                    "Package '" + basePackage + "' does not exist"
                );
            }

            List<Class<?>> controllers = new ArrayList<>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File directory = new File(resource.getFile());
                findControllersInDirectory(directory, basePackage, controllers);
            }

            if (controllers.isEmpty()) {
                throw new SprintFrameworkException(
                    ErrorType.EMPTY_PACKAGE,
                    "No controllers found in package '" + basePackage + "'"
                );
            }

            return controllers;
        } catch (Exception e) {
            if (e instanceof SprintFrameworkException) {
                throw (SprintFrameworkException) e;
            }
            throw new SprintFrameworkException(
                ErrorType.PACKAGE_NOT_FOUND,
                "Error scanning package '" + basePackage + "'",
                e
            );
        }
    }

    private static void findControllersInDirectory(File directory, String basePackage, List<Class<?>> controllers) {
        if (!directory.exists()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                findControllersInDirectory(file, basePackage + "." + file.getName(), controllers);
            } else if (file.getName().endsWith(".class")) {
                String className = basePackage + "." + file.getName().substring(0, file.getName().length() - 6);
                try {
                    Class<?> cls = Class.forName(className);
                    if (cls.isAnnotationPresent(Controller.class)) {
                        controllers.add(cls);
                    }
                } catch (ClassNotFoundException ignored) {
                    // Skip classes that can't be loaded
                }
            }
        }
    }
}
