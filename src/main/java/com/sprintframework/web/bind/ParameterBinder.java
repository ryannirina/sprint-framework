package com.sprintframework.web.bind;

import com.sprintframework.web.annotation.RequestParam;
import com.sprintframework.web.exception.SprintFrameworkException;
import com.sprintframework.web.exception.SprintFrameworkException.ErrorType;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class ParameterBinder {
    
    public static Object[] bindParameters(Parameter[] parameters, HttpServletRequest request) {
        Object[] args = new Object[parameters.length];
        
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            String paramName = getParameterName(param);
            String paramValue = request.getParameter(paramName);
            
            // Check if parameter is required
            RequestParam annotation = param.getAnnotation(RequestParam.class);
            if (annotation != null && annotation.required() && paramValue == null) {
                throw new SprintFrameworkException(
                    ErrorType.PARAMETER_BINDING,
                    "Required parameter '" + paramName + "' is missing"
                );
            }
            
            args[i] = convertParameterValue(paramValue, param.getType());
        }
        
        return args;
    }
    
    private static String getParameterName(Parameter param) {
        RequestParam annotation = param.getAnnotation(RequestParam.class);
        if (annotation != null && !annotation.value().isEmpty()) {
            return annotation.value();
        }
        return param.getName();
    }
    
    private static Object convertParameterValue(String value, Class<?> targetType) {
        if (value == null) {
            return null;
        }
        
        try {
            if (targetType == String.class) {
                return value;
            } else if (targetType == int.class || targetType == Integer.class) {
                return Integer.parseInt(value);
            } else if (targetType == long.class || targetType == Long.class) {
                return Long.parseLong(value);
            } else if (targetType == double.class || targetType == Double.class) {
                return Double.parseDouble(value);
            } else if (targetType == boolean.class || targetType == Boolean.class) {
                return Boolean.parseBoolean(value);
            } else {
                throw new SprintFrameworkException(
                    ErrorType.PARAMETER_BINDING,
                    "Unsupported parameter type: " + targetType.getName()
                );
            }
        } catch (NumberFormatException e) {
            throw new SprintFrameworkException(
                ErrorType.PARAMETER_BINDING,
                "Failed to convert value '" + value + "' to type " + targetType.getName()
            );
        }
    }
}
