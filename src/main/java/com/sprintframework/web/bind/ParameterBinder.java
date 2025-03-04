package com.sprintframework.web.bind;

import com.sprintframework.web.annotation.RequestParam;
import com.sprintframework.web.annotation.RequestObject;
import com.sprintframework.web.annotation.RequestField;
import com.sprintframework.web.exception.SprintFrameworkException;
import com.sprintframework.web.exception.SprintFrameworkException.ErrorType;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ParameterBinder {
    
    public static Object[] bindParameters(Parameter[] parameters, HttpServletRequest request) {
        Object[] args = new Object[parameters.length];
        
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            
            // Check if parameter has any binding annotation
            if (!hasBindingAnnotation(param)) {
                throw new SprintFrameworkException(
                    ErrorType.PARAMETER_BINDING,
                    "Parameter '" + param.getName() + "' must be annotated with @RequestParam or @RequestObject"
                );
            }
            
            if (param.isAnnotationPresent(RequestParam.class)) {
                args[i] = bindSimpleParameter(param, request);
            } else if (param.isAnnotationPresent(RequestObject.class)) {
                args[i] = bindObjectParameter(param, request);
            }
        }
        
        return args;
    }
    
    private static boolean hasBindingAnnotation(Parameter param) {
        return param.isAnnotationPresent(RequestParam.class) || 
               param.isAnnotationPresent(RequestObject.class);
    }
    
    private static Object bindSimpleParameter(Parameter param, HttpServletRequest request) {
        String paramName = getParameterName(param);
        String paramValue = request.getParameter(paramName);
        
        RequestParam annotation = param.getAnnotation(RequestParam.class);
        if (annotation != null && annotation.required() && paramValue == null) {
            throw new SprintFrameworkException(
                ErrorType.PARAMETER_BINDING,
                "Required parameter '" + paramName + "' is missing"
            );
        }
        
        return convertParameterValue(paramValue, param.getType());
    }
    
    private static Object bindObjectParameter(Parameter param, HttpServletRequest request) {
        Class<?> type = param.getType();
        RequestObject annotation = param.getAnnotation(RequestObject.class);
        String prefix = annotation.prefix().isEmpty() ? "" : annotation.prefix() + ".";
        
        try {
            Object instance = type.getDeclaredConstructor().newInstance();
            
            for (Field field : type.getDeclaredFields()) {
                if (field.isAnnotationPresent(RequestField.class)) {
                    field.setAccessible(true);
                    RequestField fieldAnnotation = field.getAnnotation(RequestField.class);
                    String fieldName = fieldAnnotation.value().isEmpty() ? field.getName() : fieldAnnotation.value();
                    String paramName = prefix + fieldName;
                    String paramValue = request.getParameter(paramName);
                    
                    if (fieldAnnotation.required() && paramValue == null) {
                        throw new SprintFrameworkException(
                            ErrorType.PARAMETER_BINDING,
                            "Required field '" + paramName + "' is missing"
                        );
                    }
                    
                    Object convertedValue = convertParameterValue(paramValue, field.getType());
                    field.set(instance, convertedValue);
                }
            }
            
            return instance;
        } catch (ReflectiveOperationException e) {
            throw new SprintFrameworkException(
                ErrorType.PARAMETER_BINDING,
                "Failed to bind object parameter: " + e.getMessage()
            );
        }
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
