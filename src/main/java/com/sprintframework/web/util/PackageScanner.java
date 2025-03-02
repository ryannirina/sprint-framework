package com.sprintframework.web.util;

import com.sprintframework.web.annotation.Controller;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PackageScanner {
    public static List<Class<?>> findControllers(String basePackage) {
        List<Class<?>> controllers = new ArrayList<>();
        try {
            String path = basePackage.replace('.', '/');
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL resource = classLoader.getResource(path);
            
            if (resource == null) {
                throw new IllegalArgumentException("Package " + basePackage + " not found");
            }
            
            File directory = new File(resource.getFile());
            scanDirectory(directory, basePackage, controllers);
        } catch (Exception e) {
            throw new RuntimeException("Error scanning package: " + basePackage, e);
        }
        return controllers;
    }
    
    private static void scanDirectory(File directory, String basePackage, List<Class<?>> controllers) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    scanDirectory(file, basePackage + "." + file.getName(), controllers);
                } else if (file.getName().endsWith(".class")) {
                    String className = basePackage + "." + 
                        file.getName().substring(0, file.getName().length() - 6);
                    try {
                        Class<?> cls = Class.forName(className);
                        if (cls.isAnnotationPresent(Controller.class)) {
                            controllers.add(cls);
                        }
                    } catch (ClassNotFoundException e) {
                        // Skip if class cannot be loaded
                    }
                }
            }
        }
    }
}
