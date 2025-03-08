package com.sprintframework.web.controller;

import com.sprintframework.web.annotation.Controller;
import com.sprintframework.web.annotation.GetMapping;
import com.sprintframework.web.handler.HandlerMapping;
import com.sprintframework.web.util.PackageScanner;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SystemController {
    
    private final HandlerMapping handlerMapping;
    private final String basePackage;

    public SystemController(HandlerMapping handlerMapping, String basePackage) {
        this.handlerMapping = handlerMapping;
        this.basePackage = basePackage;
    }

    @GetMapping("/controllers")
    public String listControllers() {
        List<Class<?>> controllers = PackageScanner.findControllers(basePackage);
        
        StringBuilder response = new StringBuilder();
        response.append("Available Controllers:\n\n");
        
        for (Class<?> controller : controllers) {
            response.append("* ").append(controller.getName()).append("\n");
            response.append("  Mappings:\n");
            
            // Get all mappings for this controller
            handlerMapping.getHandlersByController(controller).forEach((url, method) -> {
                response.append("  - ").append(url).append(" -> ")
                       .append(method.getName()).append("\n");
            });
            response.append("\n");
        }
        
        return response.toString();
    }
}
