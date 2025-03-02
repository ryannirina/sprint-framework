package com.sprintframework.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FrontController extends HttpServlet {
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get the request URI
        String uri = request.getRequestURI();
        
        // TODO: In future sprints, we'll implement:
        // 1. Handler mapping
        // 2. Controller execution
        // 3. View resolution
        
        // For now, just echo back the requested URI
        response.setContentType("text/plain");
        response.getWriter().write("Sprint Framework - Requested URI: " + uri);
    }
}
