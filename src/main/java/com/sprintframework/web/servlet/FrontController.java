package com.sprintframework.web.servlet;

import com.sprintframework.web.util.PackageScanner;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class FrontController extends HttpServlet {
    private List<Class<?>> controllers;
    
    @Override
    public void init() throws ServletException {
        String basePackage = getServletConfig().getInitParameter("basePackage");
        if (basePackage == null || basePackage.trim().isEmpty()) {
            throw new ServletException("basePackage parameter must be specified in web.xml");
        }
        controllers = PackageScanner.findControllers(basePackage);
    }
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        
        if ("/controllers".equals(uri)) {
            response.setContentType("text/plain");
            StringBuilder sb = new StringBuilder();
            sb.append("Available Controllers:\n\n");
            
            for (Class<?> controller : controllers) {
                sb.append("- ").append(controller.getName()).append("\n");
            }
            
            response.getWriter().write(sb.toString());
        } else {
            response.setContentType("text/plain");
            response.getWriter().write("Sprint Framework - Requested URI: " + uri);
        }
    }
}
