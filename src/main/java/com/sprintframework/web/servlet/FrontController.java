package com.sprintframework.web.servlet;

import com.sprintframework.web.handler.HandlerMapping;
import com.sprintframework.web.handler.HandlerMethod;
import com.sprintframework.web.modelview.ModelView;
import com.sprintframework.web.util.PackageScanner;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FrontController extends HttpServlet {
    private HandlerMapping handlerMapping;
    
    @Override
    public void init() throws ServletException {
        String basePackage = getServletConfig().getInitParameter("basePackage");
        if (basePackage == null || basePackage.trim().isEmpty()) {
            throw new ServletException("basePackage parameter must be specified in web.xml");
        }
        
        List<Class<?>> controllers = PackageScanner.findControllers(basePackage);
        handlerMapping = new HandlerMapping();
        
        for (Class<?> controller : controllers) {
            handlerMapping.registerController(controller);
        }
    }
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String relativeUrl = uri.substring(contextPath.length());
        
        HandlerMethod handler = handlerMapping.getHandler(relativeUrl);
        
        if (handler != null) {
            try {
                Object controller = handler.getControllerClass().getDeclaredConstructor().newInstance();
                Object result = handler.getMethod().invoke(controller);
                
                if (result instanceof ModelView) {
                    handleModelView((ModelView) result, request, response);
                } else {
                    // Handle non-ModelView returns as before
                    response.setContentType("text/plain");
                    response.getWriter().write(String.valueOf(result));
                }
            } catch (Exception e) {
                throw new ServletException("Error processing request", e);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("No handler found for URL: " + relativeUrl);
        }
    }

    private void handleModelView(ModelView modelView, HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Bind all attributes from the model to the request
        for (Map.Entry<String, Object> entry : modelView.getData().entrySet()) {
            request.setAttribute(entry.getKey(), entry.getValue());
        }
        
        // Forward the request to the view
        String viewPath = "/WEB-INF/views/" + modelView.getUrl();
        request.getRequestDispatcher(viewPath).forward(request, response);
    }
}
