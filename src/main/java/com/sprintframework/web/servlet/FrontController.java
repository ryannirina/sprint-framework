package com.sprintframework.web.servlet;

import com.sprintframework.web.handler.HandlerMapping;
import com.sprintframework.web.handler.HandlerMethod;
import com.sprintframework.web.modelview.ModelView;
import com.sprintframework.web.util.PackageScanner;
import com.sprintframework.web.exception.SprintFrameworkException;
import com.sprintframework.web.exception.SprintFrameworkException.ErrorType;
import com.sprintframework.web.bind.ParameterBinder;
import com.sprintframework.web.annotation.SessionInject;
import com.sprintframework.web.session.Session;
import com.sprintframework.web.registry.ControllerRegistry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Parameter;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class FrontController extends HttpServlet {
    private HandlerMapping handlerMapping;
    private String basePackage;
    private static final String CONTROLLERS_URL = "/controllers";

    @Override
    public void init() throws ServletException {
        try {
            basePackage = getServletConfig().getInitParameter("basePackage");
            if (basePackage == null || basePackage.trim().isEmpty()) {
                throw new SprintFrameworkException(
                    ErrorType.PACKAGE_NOT_FOUND,
                    "basePackage parameter must be specified in web.xml"
                );
            }
            
            handlerMapping = new HandlerMapping();
            
            // Register application controllers
            List<Class<?>> controllers = PackageScanner.findControllers(basePackage);
            for (Class<?> controller : controllers) {
                handlerMapping.registerController(controller);
            }
        } catch (SprintFrameworkException e) {
            throw new ServletException("Failed to initialize FrontController: " + e.getMessage(), e);
        }
    }
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SprintFrameworkException e) {
            handleError(e, request, response);
        }
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String relativeUrl = uri.substring(contextPath.length());
        
        // Special handling for /controllers endpoint
        if (CONTROLLERS_URL.equals(relativeUrl)) {
            handleControllersList(response);
            return;
        }
        
        try {
            HandlerMethod handler = handlerMapping.getHandler(relativeUrl);
            Object controller = ControllerRegistry.getInstance().getController(handler.getControllerClass());
            
            // Inject session if needed
            injectSession(controller, request);
            
            // Bind parameters and invoke the method
            Object[] args = ParameterBinder.bindParameters(handler.getParameters(), request);
            Object result = handler.getMethod().invoke(controller, args);
            
            if (result == null) {
                throw new SprintFrameworkException(
                    ErrorType.UNSUPPORTED_RETURN_TYPE,
                    "Controller methods must return a value"
                );
            }
            
            if (result instanceof ModelView) {
                handleModelView((ModelView) result, request, response);
            } else if (result instanceof String || result instanceof Number || result instanceof Boolean) {
                response.setContentType("text/plain");
                response.getWriter().write(String.valueOf(result));
            } else {
                throw new SprintFrameworkException(
                    ErrorType.UNSUPPORTED_RETURN_TYPE,
                    "Unsupported return type: " + result.getClass().getName()
                );
            }
        } catch (Exception e) {
            if (e instanceof SprintFrameworkException) {
                throw (SprintFrameworkException) e;
            }
            throw new SprintFrameworkException(
                ErrorType.UNSUPPORTED_RETURN_TYPE,
                "Error processing request: " + e.getMessage(),
                e
            );
        }
    }

    private void injectSession(Object controller, HttpServletRequest request) {
        try {
            for (Field field : controller.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(SessionInject.class)) {
                    if (field.getType() != Session.class) {
                        throw new SprintFrameworkException(
                            ErrorType.PARAMETER_BINDING,
                            "@SessionInject can only be used with Session type"
                        );
                    }
                    field.setAccessible(true);
                    field.set(controller, new Session(request));
                }
            }
        } catch (IllegalAccessException e) {
            throw new SprintFrameworkException(
                ErrorType.PARAMETER_BINDING,
                "Failed to inject session: " + e.getMessage()
            );
        }
    }

    private void handleModelView(ModelView modelView, HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        for (Map.Entry<String, Object> entry : modelView.getData().entrySet()) {
            request.setAttribute(entry.getKey(), entry.getValue());
        }
        
        String viewPath = "/WEB-INF/views/" + modelView.getUrl();
        request.getRequestDispatcher(viewPath).forward(request, response);
    }

    private void handleControllersList(HttpServletResponse response) throws IOException {
        List<Class<?>> controllers = PackageScanner.findControllers(basePackage);
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        writer.println("Available Controllers:");
        for (Class<?> controller : controllers) {
            writer.println("- " + controller.getName());
        }
    }

    private void handleError(SprintFrameworkException e, HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        response.setContentType("text/plain");
        
        switch (e.getErrorType()) {
            case URL_NOT_FOUND:
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                break;
            case PACKAGE_NOT_FOUND:
            case EMPTY_PACKAGE:
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                break;
            case DUPLICATE_URL:
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                break;
            case UNSUPPORTED_RETURN_TYPE:
            case PARAMETER_BINDING:
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                break;
            default:
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        
        response.getWriter().write(e.getErrorType().getDescription() + ": " + e.getMessage());
    }
}
